package com.example.bookapi.service;

import com.example.bookapi.dto.ApiResponse;
import com.example.bookapi.dto.BookDto;
import com.example.bookapi.entity.Book;
import com.example.bookapi.entity.Group;
import com.example.bookapi.entity.attachmant.AttachmantFile;
import com.example.bookapi.entity.attachmant.AttachmantFoto;
import com.example.bookapi.entity.enums.LanguageEnum;
import com.example.bookapi.repositoriy.AttachmantFileRepositary;
import com.example.bookapi.repositoriy.AttachmantFotoRepositary;
import com.example.bookapi.repositoriy.BookRepositary;
import com.example.bookapi.repositoriy.GroupRepositary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

  final  BookRepositary bookRepositary;
  final  GroupRepositary groupRepositary;
  final AttachmantFotoRepositary attachmantFotoRepositary;
  final AttachmantFileRepositary attachmantFileRepositary;


    public ApiResponse add(BookDto bookDto) throws IOException {
        if (bookDto.getFoto().isEmpty() || bookDto.getFile().isEmpty()) {
            return ApiResponse.builder()
                    .message("File or picture of book must not be empty")
                    .build();
        }
        if (!Objects.requireNonNull(bookDto.getFile().getOriginalFilename()).matches("^(.+)\\.(pdf|epub|word|fb2)$")) {
            return ApiResponse.builder()
                    .message("File type must be pdf, epub, word, fb2, txt")
                    .build();
        }
        if (!Objects.requireNonNull(bookDto.getFoto().getOriginalFilename()).matches("^(.+)\\.(png|jpeg|ico|jpg)$")) {
            return ApiResponse.builder()
                    .message("File type must be png, jpeg, ico, jpg")
                    .build();
        }
        if(!bookDto.getLanguage().equals(LanguageEnum.ENG) && !bookDto.getLanguage().equals(LanguageEnum.RUS) && !bookDto.getLanguage().equals(LanguageEnum.UZB)){
            System.out.println(LanguageEnum.UZB.getClass().getSimpleName());
            System.out.println(bookDto.getLanguage().getClass().getSimpleName());
            return ApiResponse.builder()
                    .message("Language Not found")
                    .success(false)
                    .build();
        }
        //File

        MultipartFile file =bookDto.getFile();

        AttachmantFile file1 = new AttachmantFile();
        file1.setBytes(file.getBytes());
        file1.setContentType(file.getContentType());
        file1.setSize(file.getSize());
        file1.setName(file.getOriginalFilename());
        //Foto

        MultipartFile foto =bookDto.getFoto();

        AttachmantFoto foto1 = new AttachmantFoto();
        foto1.setSize(file.getSize());
        foto1.setContentType(file.getContentType());
        foto1.setBytes(file.getBytes());
        foto1.setName(file.getOriginalFilename());

        Optional<Group> group = groupRepositary.findById(bookDto.getGroupNumber());


        Book book = new Book();
        book.setGroupNumber(group.get());
        book.setBookFoto(foto1);
        book.setBookFile(file1);
        book.setBookName(bookDto.getName());
        book.setLanguage(bookDto.getLanguage());

        Book save = bookRepositary.save(book);

            return ApiResponse.builder()
                    .message("Created!")
                    .success(true)
                    .obj(save)
                    .build();
    }

    public ApiResponse edit(BookDto bookDto, Integer id) throws IOException {
        if(!bookRepositary.existsById(id)){
            return ApiResponse.builder()
                    .message("Book Not found")
                    .build();
        }
        if (bookDto.getFoto().isEmpty() || bookDto.getFile().isEmpty()) {
            return ApiResponse.builder()
                    .message("File or picture of book must not be empty")
                    .build();
        }
        if (!Objects.requireNonNull(bookDto.getFile().getOriginalFilename()).matches("^(.+)\\.(pdf|epub|word|fb2)$")) {
            return ApiResponse.builder()
                    .message("File type must be pdf, epub, word, fb2, txt")
                    .build();
        }
        if (!Objects.requireNonNull(bookDto.getFoto().getOriginalFilename()).matches("^(.+)\\.(png|jpeg|ico|jpg)$")) {
            return ApiResponse.builder()
                    .message("File type must be png, jpeg, ico, jpg")
                    .build();
        }
        if(!bookDto.getLanguage().equals(LanguageEnum.ENG) && !bookDto.getLanguage().equals(LanguageEnum.RUS) && !bookDto.getLanguage().equals(LanguageEnum.UZB)){
            System.out.println(LanguageEnum.UZB.getClass().getSimpleName());
            System.out.println(bookDto.getLanguage().getClass().getSimpleName());
            return ApiResponse.builder()
                    .message("Language Not found")
                    .success(false)
                    .build();
        }
        Optional<Group> group = groupRepositary.findById(bookDto.getGroupNumber());
        Optional<Book> byId = bookRepositary.findById(id);
        Book book = byId.get();


        MultipartFile file = bookDto.getFile();
        AttachmantFile file1 = book.getBookFile();

        file1.setSize(file.getSize());
        file1.setName(file.getOriginalFilename());
        file1.setBytes(file.getBytes());
        file1.setContentType(file.getContentType());


        MultipartFile foto = bookDto.getFoto();
        AttachmantFoto foto1 = book.getBookFoto();

        foto1.setName(foto.getOriginalFilename());
        foto1.setContentType(foto.getContentType());
        foto1.setBytes(foto.getBytes());
        foto1.setSize(foto.getSize());


        book.setGroupNumber(group.get());
        book.setLanguage(bookDto.getLanguage());
        book.setBookName(bookDto.getName());
        book.setBookFile(file1);
        book.setBookFoto(foto1);

        bookRepositary.save(book);

        return ApiResponse.builder()
                .message("Updated")
                .success(true)
                .build();
    }
}