package com.example.finalprojectbackend.lab2you;

public class Lab2YouConstants {

    public static final String _LAB2YOU_REASON_CODE_UNEXPECTED_EXCEPTION = "there was an unexpected exception";
    public enum lab2YouErrorCodes {

        USER_ALREADY_EXISTS("The user already exists"),
        USER_NOT_FOUND("The user was not found"),

        EMAIL_NOT_SENT("The registration email was not sent"),

        USER_NOT_CREATED("The user was not created due to invalid data sent"),

        USER_NOT_UPDATED("The user was not updated"),

        USER_NOT_DELETED("The user was not deleted"),

        USER_NOT_AUTHENTICATED("The user was not authenticated"),
        ACTION_UNSUPPORTED("The action is not supported"),
        INVALID_DATA("The data sent is invalid"),
        EMAIL_ALREADY_EXISTS("The email already exists" +
                " please use another email address "+ " or contact the administrator for more details");

        String description;

        public String getDescription() {
            return this.description;
        }

        lab2YouErrorCodes(String description) {
            this.description = description;
        }
    }

    public enum successCodes {
        EMAIL_SENT("el correo de confirmacion fue enviado a su correo electronico"),
        USER_CREATED("Usuario creado exitosamente"),
        SAMPLE_CREATED("La muestra se agrego exitosamente"),
        REGISTRATION_SUCCESS("Usuario registrado exitosamente");

        String description;

        public String getDescription() {
            return this.description;
        }

        successCodes(String description) {
            this.description = description;
        }
    }

    public enum lab2YouRoles {
        ADMIN("Admin"),
        USER("user"),
        ANALYST("Analista"),
        MEDICAL("Medico"),
        TECHNICIAN("Tecnico"),
        CENTRALIZER("Centralizador");

        private String role;

        lab2YouRoles(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }

    }

    public enum Authority {

        CREATE_REQUEST("CREATE_REQUEST"),
        UPDATE_REQUEST("UPDATE_REQUEST"),
        DELETE_REQUEST("DELETE_REQUEST"),
        READ_REQUEST("READ_REQUEST"),

        CREATE_SAMPLE("CREATE_SAMPLE"),
        UPDATE_SAMPLE("UPDATE_SAMPLE"),
        DELETE_SAMPLE("DELETE_SAMPLE"),
        READ_SAMPLE("READ_SAMPLE"),

        CREATE_ASSIGNMENT("CREATE_ASSIGNMENT"),
        UPDATE_ASSIGNMENT("UPDATE_ASSIGNMENT"),
        DELETE_ASSIGNMENT("DELETE_ASSIGNMENT"),
        READ_ASSIGNMENT("READ_ASSIGNMENT"),

        CREATE_REQUEST_STATUS("CREATE_REQUEST_STATUS"),
        UPDATE_REQUEST_STATUS("UPDATE_REQUEST_STATUS"),
        DELETE_REQUEST_STATUS("DELETE_REQUEST_STATUS"),
        READ_REQUEST_STATUS("READ_REQUEST_STATUS"),

        CREATE_CATALOG("CREATE_CATALOG"),
        UPDATE_CATALOG("UPDATE_CATALOG"),
        DELETE_CATALOG("DELETE_CATALOG"),
        READ_CATALOG("READ_CATALOG"),

        CREATE_ANALYST("CREATE_ANALYSIS"),
        UPDATE_ANALYST("UPDATE_ANALYSIS"),
        DELETE_ANALYST("DELETE_ANALYSIS"),
        READ_ANALYST("READ_ANALYSIS"),

        CREATE_ANALYSIS_DOCUMENT("CREATE_ANALYSIS_DOCUMENT"),
        UPDATE_ANALYSIS_DOCUMENT("UPDATE_ANALYSIS_DOCUMENT"),
        DELETE_ANALYSIS_DOCUMENT("DELETE_ANALYSIS_DOCUMENT"),
        READ_ANALYSIS_DOCUMENT("READ_ANALYSIS_DOCUMENT"),

        CREATE_EMPLOYEE("CREATE_EMPLOYEE"),
        UPDATE_EMPLOYEE("UPDATE_EMPLOYEE"),
        DELETE_EMPLOYEE("DELETE_EMPLOYEE"),
        READ_EMPLOYEE("READ_EMPLOYEE");


        private String authority;

        Authority(String authority) {
            this.authority = authority;
        }

        public String getAuthority() {
            return authority;
        }

    }

    public enum userTypes {
        CUSTOMER("externo"),
        EMPLOYEE("interno");
        private String userType;
        userTypes(String userType) {
            this.userType = userType;
        }

        public String getUserType() {
            return userType;
        }

    }

    public enum operationTypes {
        CREATE("CREATE"),
        UPDATE("UPDATE"),
        DELETE("DELETE"),
        READ("READ");
        private String operation;
        operationTypes(String operation) {
            this.operation = operation;
        }
        public String getOperationType() {
            return operation;
        }

    }

    public enum statusTypes {
        CREATED("Creado"),
        ANALYSIS("Análisis");
        String status;

        statusTypes(String status) {
            this.status = status;
        }

        public String getStatusType() {
            return status;
        }

    }

}
