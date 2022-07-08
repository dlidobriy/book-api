package com.example.bookapi.dto;

import com.example.bookapi.entity.attachmant.AttachmantFile;
import com.example.bookapi.entity.attachmant.AttachmantFoto;
import com.example.bookapi.entity.enums.LanguageEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDto {

    private Integer id;

    public BookDto(String name, Integer groupNumber, LanguageEnum language, MultipartFile file, MultipartFile foto) {
        this.name = name;
        this.groupNumber = groupNumber;
        this.language = language;
        this.file = file;
        this.foto = foto;
    }

    private String name;

    private Integer groupNumber;
    @Enumerated(EnumType.STRING)
    private LanguageEnum language;

    private MultipartFile file;

    private MultipartFile foto;

}
