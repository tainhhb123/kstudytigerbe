package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.DocumentItemRequest;
import org.example.ktigerstudybe.dto.resp.DocumentItemResponse;
import org.example.ktigerstudybe.service.documentItem.DocumentItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-items")
public class DocumentItemController {

}
