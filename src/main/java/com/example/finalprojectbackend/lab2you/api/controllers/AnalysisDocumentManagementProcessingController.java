package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.dto.AnalysisDocumentDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.SampleEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.PDFSummary;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.SampleRepository;
import com.example.finalprojectbackend.lab2you.providers.CurrentUserProvider;
import com.example.finalprojectbackend.lab2you.service.AnalysisDocumentService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.operationTypes.*;

@RestController
@RequestMapping("/api/v1/analysis-document")
public class AnalysisDocumentManagementProcessingController {

    private final AnalysisDocumentService analysisDocumentService;
    private final SampleRepository sampleRepository;

    private final CurrentUserProvider currentUserProvider;

    public AnalysisDocumentManagementProcessingController(AnalysisDocumentService analysisDocumentService,
                                                          SampleRepository sampleRepository, CurrentUserProvider currentUserProvider) {
        this.analysisDocumentService = analysisDocumentService;
        this.sampleRepository = sampleRepository;
        this.currentUserProvider = currentUserProvider;

    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getAll() {
        ResponseWrapper responseWrapper = analysisDocumentService.executeReadAll();
        return ResponseEntity.ok(responseWrapper);
    }

    @GetMapping("/{sampleId}")
    public ResponseEntity<ResponseWrapper> getDocumentsBySampleId(@PathVariable Long sampleId) {
        ResponseWrapper responseWrapper = analysisDocumentService.findAllDocumentsBySampleId(sampleId);
        return ResponseEntity.ok(responseWrapper);
    }

    @GetMapping("generalInformationForPDF/{sampleId}")
    public ResponseEntity<Map<String,Map<String,String>>> getGeneralInformationForPDF(@PathVariable Long sampleId) {
        UserEntity userLogin = currentUserProvider.getCurrentUser();
        PDFSummary pdfSummary = new PDFSummary();
        Map<String,String> header = new HashMap<>();
        Map<String,String> externalUser = new HashMap<>();
        Map<String,String> sample = new HashMap<>();
        SampleEntity sampleEntity = sampleRepository.findById(sampleId).orElse(null);

        if (sampleEntity == null || sampleEntity.isDeleted()) {
            return ResponseEntity.notFound().build();
        }

        header.put("Codigo Solictud", sampleEntity.getRequest().getRequestCode());
        header.put("No. Expediente", sampleEntity.getRequest().getCustomer().getExpedientNumber());
        header.put("Solicitante", userLogin.getEmployee().getFirstName()+" "+userLogin.getEmployee().getLastName());

        externalUser.put("Nombre", sampleEntity.getRequest().getCustomer().getFirstName()+" "+sampleEntity.getRequest().getCustomer().getLastName());
        externalUser.put("Telefono", sampleEntity.getRequest().getCustomer().getPhoneNumber());
        externalUser.put("Correo", sampleEntity.getRequest().getCustomer().getUser().getEmail());
        externalUser.put("Nit", sampleEntity.getRequest().getCustomer().getNit());

        sample.put("Codigo Muestra", sampleEntity.getId().toString());
        sample.put("Tipo de Muestra", sampleEntity.getSampleTypeEntity().getName());
        sample.put("Fecha de Recepcion", sampleEntity.getCreatedAt().toString());
        sample.put("Cantidad de unidades", String.valueOf(sampleEntity.getSampleItemEntities().size()));
        sample.put("Presentacion",sampleEntity.getPresentation());

        pdfSummary.add("Encabezado",header);
        pdfSummary.add("Usuario Externo",externalUser);
        pdfSummary.add("Muestra",sample);

        return ResponseEntity.ok(pdfSummary.getPdfSummary());
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadAnalysisDocument(@PathVariable Long documentId) {
        try {

            AnalysisDocumentEntity analysisDocumentEntity = analysisDocumentService.findById(documentId);

            if (analysisDocumentEntity == null) {
                return ResponseEntity.notFound().build();
            }
            if (analysisDocumentEntity.getIsDeleted()) {
                return ResponseEntity.notFound().build();
            }

            if (analysisDocumentEntity.getSample().isDeleted()) {
                return ResponseEntity.notFound().build();
            }

            String filePath = analysisDocumentEntity.getPath();

            Resource resource = new FileSystemResource(filePath);

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }


    @PostMapping("/upload")
    public ResponseEntity<ResponseWrapper> upload(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("sampleId") Long sampleId,
                                                  @RequestParam("resolution") String resolution,
                                                  @RequestParam("analysisDocumentType") Long analysisDocumentTypeId) {

        try {


            SampleEntity sampleEntity = sampleRepository.findById(sampleId).orElse(null);

            if (sampleEntity == null) {
                return ResponseEntity.badRequest().body(new ResponseWrapper(false, "La muestra no existe", null));
            }

            String recentStatus = sampleEntity.getRequest().getRequestStatuses()
                    .stream()
                    .min((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                    .map(status -> status.getStatus().getName())
                    .orElse(null);

            boolean statusAnalysis = Lab2YouConstants.statusTypes.ANALYSIS.getStatusType().equalsIgnoreCase(recentStatus);

            if (!statusAnalysis) {
                return ResponseEntity.badRequest().body(new ResponseWrapper(false, "Solo se permite cargar documentos a la muestra en estado analisis", null));
            }

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseWrapper(false, "El archivo esta vacio", null));
            }

            if (Lab2YouUtils.isFileEncryptedOrEmptyBody(file)) {
                return ResponseEntity.badRequest().body(new ResponseWrapper(false, "el archivo podria estar encriptado, o su contenido esta vacio", null));
            }

            AnalysisDocumentDTO analysisDocumentDTO = new AnalysisDocumentDTO(sampleId, resolution, analysisDocumentTypeId);

            AnalysisDocumentEntity analysisDocumentEntity = analysisDocumentService.mapToEntityAnalysisDocument(analysisDocumentDTO);

            Path filePath = getPath(file, sampleId);
            analysisDocumentEntity.setPath(filePath.toString());

            ResponseWrapper responseWrapper = analysisDocumentService.validate(analysisDocumentEntity, CREATE.getOperationType());

            if (!responseWrapper.getErrors().isEmpty()) {
                return ResponseEntity.badRequest().body(responseWrapper);
            }

            responseWrapper = analysisDocumentService.execute(analysisDocumentEntity, CREATE.getOperationType());

            return ResponseEntity.ok(responseWrapper);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "Error cargando el archivo", null));
        }
    }


    private Path getPath(MultipartFile file, Long sampleId) throws IOException {
        String fileName = file.getOriginalFilename();
        String uniqueFilename = generateUniqueFilename(fileName, sampleId);
        Path uploadPath = Paths.get("files/uploads"); // Corrected directory path
        Path filePath = uploadPath.resolve(uniqueFilename);

        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath);

        return filePath;
    }

    private String generateUniqueFilename(String originalFilename, Long sampleId) {
        return sampleId + "_" + System.currentTimeMillis() + "_" + originalFilename;
    }
}
