package pl.zajavka.api.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageUploadController {
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        // Tutaj możesz obsłużyć przesłane zdjęcie, np. zapisać w systemie plików lub bazie danych
        return ResponseEntity.ok("Image uploaded successfully.");
    }
}
