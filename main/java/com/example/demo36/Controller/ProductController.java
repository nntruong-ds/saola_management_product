package com.example.demo36.Controller;

import com.example.demo36.Entity.Product;
import com.example.demo36.Service.DTO.DtoProductCreate;
import com.example.demo36.Service.DTO.DtoProductGet;
import com.example.demo36.Service.DTO.DtoProductUpdate;
import com.example.demo36.Service.Filter.FilterProduct;
import com.example.demo36.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.saolasoft.base.api.method.AuditableDtoAPIMethod;
import vn.saolasoft.base.api.response.APIListResponse;
import vn.saolasoft.base.api.response.APIResponse;
import vn.saolasoft.base.api.response.APIResponseHeader;
import vn.saolasoft.base.api.response.APIResponseStatus;
import vn.saolasoft.base.service.filter.PaginationInfo;

import java.util.List;
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final AuditableDtoAPIMethod<DtoProductGet, Product, Long> truongapi;
    private final ProductService proservice;
    public ProductController(ProductService proservice) {
        this.proservice = proservice;
        // truongapi ket noi voi db
        this.truongapi = new AuditableDtoAPIMethod<>(proservice);
    }
    // ───────────────────────────────────────────────
    // GET /api/products?firstRow=20&maxResults=10&orderBy=+name
    // Lấy danh sách sản phẩm chưa void, có phân trang
    // ───────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<APIListResponse<List<DtoProductGet>>> getList(
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "5")  int size,
            @RequestParam(defaultValue = "")    String orderBy) {

        PaginationInfo pageInfo = new PaginationInfo(page, size, orderBy);
        return truongapi.getList(pageInfo);
    }
    // ───────────────────────────────────────────────
    // GET /api/products/{id}
    // Lấy chi tiết 1 sản phẩm theo id
    // ───────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<DtoProductGet>> getById(@PathVariable Long id) {
        return truongapi.getById(id);
    }
    // ───────────────────────────────────────────────
    // POST /api/products
    // Tạo sản phẩm mới
    // Header: X-User-Id: 1
    // Body: { "name": "...", "price": 100000, ... }
    // ───────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<APIResponse<Long>> create(
            @Valid @RequestBody DtoProductCreate dto,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long callerId) {

        return truongapi.create(dto, callerId);
    }
    // ───────────────────────────────────────────────
    // PUT /api/products
    // Cập nhật sản phẩm (phải có id trong body)
    // Header: X-User-Id: 1
    // Body: { "id": "...", "name": "...", "price": 200000, ... }
    // ───────────────────────────────────────────────
    @PutMapping
    public ResponseEntity<APIResponse<Long>> update(
            @Valid @RequestBody DtoProductUpdate dto,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long callerId) {

        return truongapi.update(dto, callerId);
    }
    // ───────────────────────────────────────────────
    // DELETE /api/products/{id}
    // Xóa mềm sản phẩm (voided = true, vẫn còn trong DB)
    // Header: X-User-Id: 1
    // ───────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Long>> delete(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", defaultValue = "1") Long callerId) {

        return truongapi.delete(id, callerId);
    }
    // kiểm tra role từ context holder

    private boolean isAdmin() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth != null && auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<APIListResponse<List<DtoProductGet>>> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(required = false) Boolean voided,
            @RequestParam(defaultValue = "0") int firstRow,
            @RequestParam(defaultValue = "20") int maxResults,
            @RequestParam(defaultValue = "") String orderBy) {
        FilterProduct filter = new FilterProduct();
        filter.setQuery(query);

        if (isAdmin()) {
            filter.setVoided(voided); // admin có thể lọc cả voided và unvoided
        } else {
            filter.setVoided(false); // user chỉ thấy unvoided
        }

        PaginationInfo pageInfo = new PaginationInfo(firstRow, maxResults, orderBy);
        return truongapi.search(filter, pageInfo);
    }
    // Lấy userId hiện tại từ JWT trong SecurityContext
    private Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Long id) return id;
        throw new IllegalStateException("No authenticated user in context");
    }
    // DELETE /api/products/{id}/hard  — xóa cứng, chỉ admin mới được phép
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<APIResponse<String>> hardDelete(@PathVariable String id) {
        proservice.deleteByID(Long.parseLong(id), true, currentUserId());
        return ResponseEntity.ok(new APIResponse<>(
                new APIResponseHeader(APIResponseStatus.DELETED, "Product deleted permanently"),
                id));
    }
}
