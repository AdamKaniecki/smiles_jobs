package pl.zajavka.infrastructure.database.entity;

public enum IT_Specializations {


    WEB_DEVELOPMENT("Web development"),
    MOBILE_APP_DEVELOPMENT("Mobile app development"),
    DATA_SCIENCE("Data science"),
    MACHINE_LEARNING("Machine learning"),
    CYBER_SECURITY("Cyber Security"),
    CLOUD_COMPUTING("Cloud computing"),
    DEVOPS("DevOps"),
    DATABASE_ADMINISTRATION("Database administration"),
    SOFTWARE_TESTING("Software testing"),
    ARTIFICIAL_INTELLIGENCE("Artificial intelligence"),
    NETWORK_ENGINEERING("Network engineering"),
    FRONTEND_DEVELOPMENT("Front-end development"),
    BACKEND_DEVELOPMENT("Back-end development"),
    FULLSTACK_DEVELOPMENT("Full-stack development"),
    UI_UX_DESIGN("UI/UX design"),
    GAME_DEVELOPMENT("Game development"),
    EMBEDDED_SYSTEMS("Embedded systems"),
    ROBOTICS("Robotics"),
    BUSINESS_INTELLIGENCE("Business intelligence"),
    IT_PROJECT_MANAGEMENT("IT project management"),
    IT_CONSULTING("IT consulting"),
    BIG_DATA_ANALYTICS("Big data analytics"),
    DIGITAL_MARKETING("Digital marketing"),
    INFORMATION_SECURITY("Information security"),
    COMPUTER_VISION("Computer vision"),
    NATURAL_LANGUAGE_PROCESSING("Natural language processing"),
    ENTERPRISE_SOFTWARE_DEVELOPMENT("Enterprise software development"),
    SYSTEM_ADMINISTRATION("System administration"),
    QUALITY_ASSURANCE("Quality assurance"),
    IT_SUPPORT("IT support");

    private final String displayName;

    IT_Specializations(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
