package org.example.ktigerstudybe.service.documentReport;

import lombok.RequiredArgsConstructor;
import org.example.ktigerstudybe.dto.req.DocumentReportRequest;
import org.example.ktigerstudybe.dto.resp.DocumentReportResponse;
import org.example.ktigerstudybe.model.DocumentList;
import org.example.ktigerstudybe.model.DocumentReport;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.DocumentListRepository;
import org.example.ktigerstudybe.repository.DocumentReportRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentReportServiceImpl implements DocumentReportService {

    private final DocumentReportRepository documentReportRepository;
    private final UserRepository userRepository;
    private final DocumentListRepository documentListRepository;

    @Override
    public DocumentReportResponse createReport(DocumentReportRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        DocumentList documentList = documentListRepository.findById(request.getListId())
                .orElseThrow(() -> new IllegalArgumentException("DocumentList not found"));

        DocumentReport report = DocumentReport.builder()
                .user(user)
                .documentList(documentList)
                .reason(request.getReason())
                .build();

        DocumentReport saved = documentReportRepository.save(report);

        return mapToResponse(saved);
    }

    @Override
    public DocumentReportResponse getReportById(Long reportId) {
        DocumentReport report = documentReportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));
        return mapToResponse(report);
    }

    @Override
    public List<DocumentReportResponse> getAllReports() {
        return documentReportRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReport(Long reportId) {
        if (!documentReportRepository.existsById(reportId)) {
            throw new IllegalArgumentException("Report not found");
        }
        documentReportRepository.deleteById(reportId);
    }

    @Override
    public List<DocumentReportResponse> getReportsByUserId(Long userId) {
        return documentReportRepository.findByUser_UserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentReportResponse> getReportsByListId(Long listId) {
        return documentReportRepository.findByDocumentList_ListId(listId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DocumentReportResponse mapToResponse(DocumentReport report) {
        return DocumentReportResponse.builder()
                .reportId(report.getReportId())
                .userId(report.getUser().getUserId())
                .userName(report.getUser().getUserName())
                .listId(report.getDocumentList().getListId())
                .listTitle(report.getDocumentList().getTitle())
                .reason(report.getReason())
                .reportDate(report.getReportDate())
                .build();
    }
}
