package com.softserve.itacademy;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/records/update")
public class UpdateRecordServlet extends AddressBookBaseServlet {
    private String validatedFirstname;
    private String validatedLastname;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String firstname = request.getParameter("first-name");
        firstname = firstname != null ? firstname.strip() : "";

        String lastname = request.getParameter("last-name");
        lastname = lastname != null ? lastname.strip() : "";

        String address = dao.read(firstname, lastname);
        if (address == null) {
            request.setAttribute("error", String.format(
                    "Person with name '%s %s' has not been found in Address Book!",
                    firstname, lastname));
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.getRequestDispatcher("/WEB-INF/error.jsp")
                    .forward(request, response);
        } else {
            validatedFirstname = firstname;
            validatedLastname = lastname;
            request.setAttribute("firstname", firstname);  // stripped
            request.setAttribute("lastname", lastname);    // stripped
            request.setAttribute("address", address);
            request.getRequestDispatcher("/WEB-INF/update-record.jsp")
                    .forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        dao.update(validatedFirstname, validatedLastname, request.getParameter("address"));
        // TODO: check success, log if not updated (concurrent access problems)
        response.sendRedirect("/records/list");
    }
}
