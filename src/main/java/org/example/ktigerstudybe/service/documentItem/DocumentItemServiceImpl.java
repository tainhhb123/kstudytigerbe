package org.example.ktigerstudybe.service.documentItem;

import org.example.ktigerstudybe.dto.req.DocumentItemRequest;
import org.example.ktigerstudybe.dto.resp.DocumentItemResponse;
import org.example.ktigerstudybe.model.DocumentItem;
import org.example.ktigerstudybe.model.DocumentList;
import org.example.ktigerstudybe.repository.DocumentItemRepository;
import org.example.ktigerstudybe.repository.DocumentListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentItemServiceImpl implements DocumentItemService {


}
