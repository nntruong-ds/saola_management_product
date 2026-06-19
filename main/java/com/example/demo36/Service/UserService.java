package com.example.demo36.Service;


import com.example.demo36.Service.DTO.RegiterReq;
import com.example.demo36.Service.DTO.UserGet;
import com.example.demo36.Service.DTO.LoginRequest;
import com.example.demo36.Service.DTO.LoginResponse;
import com.example.demo36.Service.DTO.RegiterReq;
import com.example.demo36.Service.DTO.DtoProductUpdate;
import com.example.demo36.Entity.User;
import vn.saolasoft.base.service.VoidableDtoService;

public interface  UserService extends VoidableDtoService<UserGet, User, Long> {
    // VoidableDtoService có sẵn CRUD cơ bản

    //   getById(id)                        → lấy 1 sản phẩm (bỏ qua đã void)
    //   getById(id, includeVoided)         → lấy 1 sản phẩm (tùy chọn có lấy đã void)
    //   getAll()                           → lấy tất cả chưa void
    //   getPageOfData(pagingInfo)          → lấy có phân trang
    //   createEntry(dto, callerId)         → tạo mới
    //   updateEntry(dto, callerId)         → cập nhật
    //
    // Voidable (soft-delete):
    //   deleteByID(id, purged, callerId)   → purged=false: xóa mềm | purged=true: xóa hẳn
    //   restoreByID(id, callerId)          → khôi phục bản ghi đã xóa mềm
    //
    // Search:
    //   search(filter, pagingInfo)         → tìm kiếm có lọc + phân trang

    UserGet register(RegiterReq req);

    LoginResponse login(LoginRequest req);
}
