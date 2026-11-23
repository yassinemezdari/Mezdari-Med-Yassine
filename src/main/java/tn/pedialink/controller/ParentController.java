package tn.pedialink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.pedialink.dto.ApiMessage;

@RestController
@RequestMapping("/api/parent")
public class ParentController {

    @GetMapping("/dashboard")
    public ResponseEntity<ApiMessage> dashboard() {
        return ResponseEntity.ok(new ApiMessage("Welcome to Parent Dashboard"));
    }
}
