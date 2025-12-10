package com.cjxy.las.utils;

import java.io.IOException;
import java.io.PrintWriter;


import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;



public class ResponseTool {
	private static ObjectMapper objectMapper;
    static {
    	objectMapper = new ObjectMapper();
    }

	public static void writeToClient(HttpServletResponse response,Object messMap) throws IOException {
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String jsonStr=objectMapper.writeValueAsString(messMap);
		out.write(jsonStr);
		out.flush();
		out.close();
	}
}

