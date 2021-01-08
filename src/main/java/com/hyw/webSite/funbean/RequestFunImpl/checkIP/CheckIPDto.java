package com.hyw.webSite.funbean.RequestFunImpl.checkIP;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckIPDto {
	String begIP;
	String endIP;
	int port;
	int timeout;
}
