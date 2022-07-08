package com.example.bookapi.controller;

import com.example.bookapi.dto.ApiResponse;
import com.example.bookapi.dto.BookDto;
import com.example.bookapi.entity.Book;
import com.example.bookapi.entity.Group;
import com.example.bookapi.entity.User;
import com.example.bookapi.entity.attachmant.AttachmantFile;
import com.example.bookapi.entity.attachmant.AttachmantFoto;
import com.example.bookapi.entity.enums.LanguageEnum;
import com.example.bookapi.repositoriy.BookRepositary;
import com.example.bookapi.repositoriy.GroupRepositary;
import com.example.bookapi.repositoriy.UserRepositary;
import com.example.bookapi.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    final UserRepositary userRepositary;
    final BookRepositary bookRepositary;
    final BookService bookService;
    final GroupRepositary groupRepositary;


    @PreAuthorize("hasAnyAuthority('CRUD')")
    @PostMapping
    public ResponseEntity add(@ModelAttribute BookDto bookDto) throws IOException {
        ApiResponse response = bookService.add(bookDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400).body(response);
    }

    @PreAuthorize("hasAnyAuthority('READ','CRUD')")
    @GetMapping("/{id}")
    public ResponseEntity getOne(@PathVariable Integer id) {
        if (!bookRepositary.existsById(id)) {
            return ResponseEntity.status(400).body("ID Not found");
        }
        Optional<Book> byId = bookRepositary.findById(id);
        return ResponseEntity.ok().body(byId);
    }

    @PreAuthorize("hasAnyAuthority('CRUD')")
    @PutMapping("/{id}")
    public ResponseEntity edit(@ModelAttribute BookDto bookDto, @PathVariable Integer id) throws IOException {

        ApiResponse response = bookService.edit(bookDto, id);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {

        if (!bookRepositary.existsById(id)) {
            return ResponseEntity.status(400).body("Id Not Found");
        }
        bookRepositary.deleteById(id);

        return ResponseEntity.ok().body(ApiResponse.builder()
                .success(true)
                .message("Deleted!")
                .build());
    }

    @GetMapping("/file/{id}")
    public ResponseEntity getFile(@PathVariable Integer id) {
        Optional<Book> optionalBook = bookRepositary.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        AttachmantFile file = optionalBook.get().getBookFile();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentLength(file.getSize())
                .body(file.getBytes());
    }

    @GetMapping("/picture/{id}")
    public ResponseEntity getPicture(@PathVariable Integer id) {
        Optional<Book> optionalBook = bookRepositary.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        AttachmantFoto picture = optionalBook.get().getBookFoto();
        return ResponseEntity.ok().header("Content-Type", picture.getContentType()).body(picture.getBytes());
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam(value = "q", required = false) String key,
                                 @RequestParam(value = "class", required = false) Integer classNumber,
                                 @RequestParam(value = "lang", required = false) LanguageEnum lang) {

        if (key == null && classNumber == null && lang == null) {
            return ResponseEntity.ok().body(bookRepositary.findAll());
        }
        Optional<Group> group = groupRepositary.findById(classNumber);
        if(key==null&&classNumber!=null&&lang!=null){
            List<Book> books = bookRepositary.searchBooksByGroupNumberAndLanguage(group.get(),lang);
            return ResponseEntity.ok().body(books);
        }
        key= Objects.requireNonNull(key).toUpperCase();
        if (bookRepositary.existsBookByBookNameStartsWith(key)) {
            Book book = bookRepositary.findByBookNameAndLanguageAndGroupNumber(key, lang, group.get());
            return ResponseEntity.ok().body(book);
        }
        return ResponseEntity.ok().body(bookRepositary.findAll());
        }

    @PostMapping("/{id}/favorite")
    public ResponseEntity addFavourite(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        Optional<Book> optionalBook = bookRepositary.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.badRequest().body("Book not found");
        }
        user.getFavorites().add(optionalBook.get());
        userRepositary.save(user);
        return ResponseEntity.ok().body("Success");
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity deleteFavourite(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        Optional<Book> optionalBook = bookRepositary.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.badRequest().body("Book not found");
        }
        user.getFavorites().removeIf(book -> book.getId().equals(optionalBook.get().getId()));
        userRepositary.save(user);
        return ResponseEntity.ok().body("Success!");
    }
}
