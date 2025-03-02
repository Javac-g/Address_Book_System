package com.system;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/records/create")
public class CreateRecordServlet extends AddressBookBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/create-record.jsp")
                .forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstname = request.getParameter("firstname");
        firstname = firstname != null ? firstname.strip() : "";

        String lastname = request.getParameter("lastname");
        lastname = lastname != null ? lastname.strip() : "";

        String address = request.getParameter("address");
        address = address != null ? address.strip() : "";

        if (firstname.isEmpty() || lastname.isEmpty() || address.isEmpty()) {
            request.setAttribute("error", "Blank strings are not allowed");
            request.getRequestDispatcher("/WEB-INF/create-record.jsp")
                    .include(request, response);
        } else if (!dao.create(firstname, lastname, address)) {
            request.setAttribute("error",
                    String.format("Record for '%s %s' already exists. Use Update to change the address.",
                            firstname, lastname));
            response.setCharacterEncoding("UTF-8");
            request.getRequestDispatcher("/WEB-INF/create-record.jsp")
                    .include(request, response);
        } else {
            response.sendRedirect("/records/list");
        }
    }
}
