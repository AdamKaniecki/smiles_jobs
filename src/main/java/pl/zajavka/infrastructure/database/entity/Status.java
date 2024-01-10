package pl.zajavka.infrastructure.database.entity;

public enum Status {

    WAITING_FOR_INTERVIEW,
    ACCEPT,
    CHOOSE_OTHER,
    PENDING; // Dodajemy stan oczekiwania (opcjonalnie)

    // Tutaj możesz dodać dodatkowe metody, jeśli są potrzebne
}