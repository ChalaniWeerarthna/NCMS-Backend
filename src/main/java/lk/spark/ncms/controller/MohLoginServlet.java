package lk.spark.ncms.controller;


import lk.spark.ncms.dao.DoctorDao;
import lk.spark.ncms.dao.MohDao;
import lk.spark.ncms.bean.Doctor;
import lk.spark.ncms.bean.Moh;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.PrintWriter;

@WebServlet(name = "MohLoginServlet")
public class MohLoginServlet extends HttpServlet {

    ///////////////// Moh login control//////////////////

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String password = req.getParameter("moh_id");
        String email = req.getParameter("email");

        Moh moh = new Moh();
        moh.setMoh_id(password);
        moh.setEmail(email);

        MohDao userDao = new MohDao();
        String userLogin = userDao.loginMoh(moh);


        if (userLogin.equals("SUCCESS")) {
            System.out.println("Success");

            Moh moh2 = new Moh(password);
            moh2.getModel();

            PrintWriter printWriter = resp.getWriter();
            printWriter.println(moh2.toString());

        } else {
            System.out.println("Failed");
        }
    }

}
