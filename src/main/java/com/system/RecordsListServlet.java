package com.system;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

@WebServlet("/records/list")
public class RecordsListServlet extends AddressBookBaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String sort = request.getParameter("sort");
        if ("desc".equals(sort)) {
            dao.sortedBy(AddressBook.SortOrder.DESC);
        } else if ("asc".equals(sort)) {
            dao.sortedBy(AddressBook.SortOrder.ASC);
        }

        List<String[]> records = new ArrayList<>();

        for (String record : dao) {
            Matcher m = PATTERN.matcher(record);
            if (m.matches() && m.groupCount() == 3) {
                records.add(new String[] {m.group(1), m.group(2), m.group(3)});
            } else {
                // TODO: log the corrupted record (can't parse)
            }
        }

        request.setAttribute("records", records);

        request.getRequestDispatcher("/WEB-INF/records-list.jsp")
                .forward(request, response);
    }
}
