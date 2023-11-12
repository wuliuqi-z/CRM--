package com.powernode.crm.workbench.service;

import javax.servlet.http.HttpServletRequest;

public interface ClueRemarkService {
    void queryClueRemarkForDetailByClueId(String ClueId, HttpServletRequest request);
}
