package com.infopuls.tash.controllers;

import com.infopuls.tash.user.User;
import com.infopuls.tash.user.UsersMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet (name="MainController", urlPatterns={"/login", "/registration", "/news" , "/archive" , "/logout"})

public class MainController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {

        String userPath = request.getServletPath();
        String method = request.getMethod();
        UsersMap map = UsersMap.getInstance();


        if (userPath.equals("/") ) {
//            if (request.getSession().getAttribute("user")!=null){
//                userPath = "/news";
//            }else{
                userPath = "/index";
//            }

        }else if (userPath.equals("/logout")){
            userPath = "/index";
            request.getSession().setAttribute("user" ,null );
        }else if (userPath.equals("/login")) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            if (login != null || password != null ) {
                if (map.checkPassword(login,password)) {
                    User current_user = map.findUserbyLogin(login);
                    request.getSession().setAttribute("user" ,current_user );
                    userPath = "/news";
                }else{
                    userPath = "/login";
                    request.setAttribute("errorText" ,"Incorrect login or password" );
                }
            }else {
                userPath = "/login";
            }

        }else if (userPath.equals("/registration")) {
                String login = request.getParameter("contact_login");
                String password = request.getParameter("contact_password");
                String confirm_password = request.getParameter("contact_confirm-password");
                String first_name = request.getParameter("contact_first_name");
                String last_name = request.getParameter("contact_last_name");
                String email = request.getParameter("contact_email");
                String phoneNumber = request.getParameter("contact_phoneNumber");
                if (login == null || password == null || confirm_password == null || first_name == null) {
                    userPath = "/registration";
                    request.setAttribute("errorText", "It is necessary to fill fields");
                } else {

                    if (map.isExistLogin(login)) {
                        userPath = "/registration";
                        request.setAttribute("errorText", "Unfortunately, this username is not available");
                    } else {
                        User current_user = new User(login, password);
                        current_user.setEmail(email);
                        current_user.setFirstName(first_name);
                        current_user.setLastName(last_name);
                        current_user.setPhoneNumber(phoneNumber);

                        map.addUser(current_user);
                       // request.setAttribute("user", current_user);
                        request.getSession().setAttribute("user" ,current_user );
                        userPath = "/news";
                    }
                }
        }else if (userPath.equals("/news")) {
            userPath = "/news";
        }else if (userPath.equals("/archive")) {
            userPath = "/archive";
        }

       if ("GET".equals(method)) {
           request.setAttribute("errorText", "");
       }
        String url = "/WEB-INF" + userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}