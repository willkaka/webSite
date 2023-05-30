package com.hyw.webSite;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class Test006 {

	public static void main(String[] args) throws Exception {

//		String s = "/** xxxx  */";
//		s.replaceAll("///*/*","").replaceAll("///*","").replaceAll("/*//","");

//		Xxdto xxdto = new Xxdto();
//		xxdto.setTime(LocalDateTime.MAX);

//		System.out.println(JSON.toJSONString(xxdto));
	}



}

@Data
class Xxdto{
	private LocalDateTime time;
}