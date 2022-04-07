package com.example.demo.controller;

import com.example.demo.dto.request_dulieunhanvao.ChangeAvatar;
import com.example.demo.dto.respond_dulieutrave.RespondMess;
import com.example.demo.model.Category;
import com.example.demo.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;
@GetMapping("/category")
    public ResponseEntity<?> pageCategory(@PageableDefault(sort = "nameCategory",
                                          direction = Sort.Direction.ASC) Pageable pageable){
    Page<Category> categoryPage= categoryService.findAll(pageable);
    if (categoryPage.isEmpty()){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return  new ResponseEntity<>(categoryPage,HttpStatus.OK);
}
    //Cach 1: Dung @PathVariable
@GetMapping("/search/{nameCategory}")
    public ResponseEntity<?> searchByNameCategory(
            @PathVariable String nameCategory,
            @PageableDefault(sort = "nameCategory", direction = Sort.Direction.ASC)Pageable pageable)
    {
        Page<Category> categoryPage = categoryService.findByNameCategoryQuery(nameCategory, pageable);
        if(categoryPage.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }
    //Cach 2: Dung @Requestparam
//    @GetMapping("/search")
//    public ResponseEntity<?> searchByNameCategory(
//    @RequestParam("nameCategory") String search,
//    @PageableDefault(sort = "nameCategory", direction = Sort.Direction.ASC)Pageable pageable){
//        Page<Category> categoryPage = categoryService.findByNameCategoryQuery(search,pageable);
//        if(categoryPage.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
//    }
@GetMapping("/category/list")
    public ResponseEntity<?> pageCategory(){
        List<Category> listCategory= categoryService.findAll();
        if(listCategory.isEmpty()){
            return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listCategory,HttpStatus.OK);
    }

@PostMapping("/category")
    public ResponseEntity<?> createCategory(@RequestBody Category category){
    if (categoryService.existsByNameCategory(category.getNameCategory())){
        return new ResponseEntity<>(new RespondMess("no_name_category"),HttpStatus.OK);
    }
    if(category.getAvatarCategory()==null){
        return new ResponseEntity<>(new RespondMess("no_avatar_category"),HttpStatus.OK);
    }

    categoryService.save(category);
    return new ResponseEntity<>(new RespondMess("create_success"),HttpStatus.OK);
}
@DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
    if(categoryService.findById(id).isPresent()){
        categoryService.deleteById(id);
        List<Category> listCategory= categoryService.findAll();
        return  new ResponseEntity<>(listCategory,HttpStatus.OK);
    }
    return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
@PutMapping("/category/edit/{id}")
    public ResponseEntity<?> changeAvatar(@PathVariable Long id, @RequestBody Category category){

        Optional<Category> newCategory= categoryService.findById(id);
        if(category.getNameCategory().equals(newCategory.get().getNameCategory()))
        {
            return new ResponseEntity<>(new RespondMess("name_existed"),HttpStatus.OK);
        }
        newCategory.get().setAvatarCategory(category.getAvatarCategory());
        newCategory.get().setNameCategory(category.getNameCategory());
        categoryService.save(newCategory.get());
          List<Category> listCategory= categoryService.findAll();
         return  new ResponseEntity<>(new RespondMess("edit_success"),HttpStatus.OK);


    }


}
