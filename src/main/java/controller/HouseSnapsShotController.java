package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import member.bean.Member;
import member.bean.Order;
import member.bean.Publish;
import service.MemberService;
import service.OrderService;
import service.PublishService;

@WebServlet("/HouseSnapsShot")
public class HouseSnapsShotController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Gson gson = new Gson();
	private int resultcode = -1; // 判斷碼
	private OrderService orderService = new OrderService();
	private PublishService publishService = new PublishService();
	private MemberService memberService = new MemberService();


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 收取client端資料
		request.setCharacterEncoding("UTF-8");
		// 回傳前端
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		StringBuilder jsonIn = new StringBuilder();
		try (BufferedReader br = request.getReader();) {
			String line = null;
			while ((line = br.readLine()) != null) {
				jsonIn.append(line);
			}
			System.out.println("input: " + jsonIn.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 拿值
		JsonObject jsonObj = gson.fromJson(jsonIn.toString(), JsonObject.class);

		try {
			resultcode = jsonObj.get("RESULTCODE").getAsInt();
		} catch (Exception e) {
			resultcode = -1;
		}
		
		
		JsonObject jsonWri = new JsonObject();
		switch (resultcode) {
		case 1: 
			int orderId = jsonObj.get("ORDERID").getAsInt();
			int signinId = jsonObj.get("SIGNINID").getAsInt(); 
			int publishId = orderService.selectPublishByID(orderId);
			Publish publish = publishService.selectById(publishId);
			Member member = memberService.selectById(signinId);
			
			jsonWri.addProperty("PUBLISH", gson.toJson(publish));
			jsonWri.addProperty("MEMBER", gson.toJson(member));
			jsonWri.addProperty("RESULT", 200);
			
			break;

		default:
			jsonWri.addProperty("RESULT", -1);
			break;
		}
		
		try (PrintWriter pw = response.getWriter();) {
			pw.println(jsonWri);
			System.out.println("output: " + jsonWri.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
