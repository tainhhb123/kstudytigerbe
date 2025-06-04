package org.example.ktigerstudybe.service.documentReport;

import org.example.ktigerstudybe.dto.req.DocumentReportRequest;
import org.example.ktigerstudybe.dto.resp.DocumentReportResponse;

import java.util.List;

public interface DocumentReportService {

    // Tạo mới một báo cáo
    DocumentReportResponse createReport(DocumentReportRequest request);

    // Lấy thông tin một báo cáo theo ID
    DocumentReportResponse getReportById(Long reportId);

    // Lấy danh sách tất cả báo cáo
    List<DocumentReportResponse> getAllReports();

    // Xóa báo cáo theo ID (ví dụ: quản trị viên xóa báo cáo đã xử lý)
    void deleteReport(Long reportId);

    // Lấy danh sách báo cáo theo người dùng
    List<DocumentReportResponse> getReportsByUserId(Long userId);

    // Lấy danh sách báo cáo theo tài liệu
    List<DocumentReportResponse> getReportsByListId(Long listId);
}
