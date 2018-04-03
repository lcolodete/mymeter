package org.mymeter.common;

import org.jpos.iso.IFB_AMOUNT;
import org.jpos.iso.IFB_BINARY;
import org.jpos.iso.IFB_BITMAP;
import org.jpos.iso.IFB_LLBINARY;
import org.jpos.iso.IFB_LLHNUM;
import org.jpos.iso.IFB_LLLBINARY;
import org.jpos.iso.IFB_LLLCHAR;
import org.jpos.iso.IFB_LLLNUM;
import org.jpos.iso.IFB_LLNUM;
import org.jpos.iso.IFB_NUMERIC;
import org.jpos.iso.IFE_CHAR;
import org.jpos.iso.IF_CHAR;
import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOFieldPackager;

public class MyMeterPackager extends ISOBasePackager {
	protected ISOFieldPackager fld[] = {
			/* 000 */new IFB_NUMERIC(4, "Message Type Indicator", true),
			/* 001 */new IFB_BITMAP(19, "Bitmap"),
			/* NAO USADO-002 */new IFB_LLNUM(19, "Primary Account number", false),
			/* 003 */new IFB_NUMERIC(6, "Processing Code", true),
			/* NAO USADO-004 */new IFB_NUMERIC(12, "Amount, Transaction", true),
			/* NAO USADO-005 */new IFB_NUMERIC(12, "AMOUNT, SETTLEMENT", true),
			/* NAO USADO-006 */new IFB_NUMERIC(12, "AMOUNT, CARDHOLDER BILLING", true),
			/* NAO USADO-007 */new IFB_NUMERIC(10, "TRANSMISSION DATE AND TIME", true),
			/* NAO USADO-008 */new IFB_NUMERIC(8, "AMOUNT, CARDHOLDER BILLING FEE",	true),
			/* NAO USADO-009 */new IFB_NUMERIC(8, "CONVERSION RATE, SETTLEMENT", true),
			/* NAO USADO-010 */new IFB_NUMERIC(8, "CONVERSION RATE, CARDHOLDER BILLING", true),
			/* 011 */new IFB_NUMERIC(6, "Systems trace audit number", true),
			/* 012 */new IFB_NUMERIC(14, "Date and time, Local transaction", true),
			/* NAO USADO-013 */new IFB_NUMERIC(4, "DATE, LOCAL TRANSACTION", true),
			/* NAO USADO-014 */new IFB_NUMERIC(4, "Date, Expiration", true),
			/* 015 */new IFB_NUMERIC(14, "DATE, SETTLEMENT", true),
			/* 016 */new IFB_NUMERIC(14, "DATE, CONVERSION", true),
			/* 017 */new IFB_NUMERIC(14, "DATE, CAPTURE", true),
			/* NAO USADO-018 */new IFB_NUMERIC(4, "Merchant type", true),
			/* NAO USADO-019 */new IFB_NUMERIC(3, "ACQUIRING INSTITUTION COUNTRY CODE",	true),
			/* NAO USADO-020 */new IFB_NUMERIC(3, "PAN EXTENDED COUNTRY CODE", true),
			/* NAO USADO-021 */new IFB_NUMERIC(3, "FORWARDING INSTITUTION COUNTRY CODE", true),
			/* NAO USADO-022 */new IFB_NUMERIC(4, "POS Entry Mode", true),
			/* NAO USADO-023 */new IFB_NUMERIC(2, "Card sequence number", true),
			/* NAO USADO-024 */new IFB_NUMERIC(3, "NII", true),
			/* NAO USADO-025 */new IFB_NUMERIC(2, "POINT OF SERVICE CONDITION CODE", true),
			/* NAO USADO-026 */new IFB_NUMERIC(2, "POINT OF SERVICE PIN CAPTURE CODE", true),
			/* NAO USADO-027 */new IFB_NUMERIC(1, "AUTHORIZATION IDENTIFICATION RESP LEN", true),
			/* NAO USADO-028 */new IFB_AMOUNT(9, "AMOUNT, TRANSACTION FEE", true),
			/* NAO USADO-029 */new IFB_AMOUNT(9, "AMOUNT, SETTLEMENT FEE", true),
			/* NAO USADO-030 */new IFB_AMOUNT(9, "AMOUNT, TRANSACTION PROCESSING FEE", true),
			/* NAO USADO-031 */new IFB_AMOUNT(9, "AMOUNT, SETTLEMENT PROCESSING FEE", true),
			/* NAO USADO-032 */new IFB_LLHNUM(11, "ACQUIRING INSTITUTION IDENT CODE", true),
			/* NAO USADO-033 */new IFB_LLBINARY(99, "Forwarding institution identification code"),
			/* NAO USADO-034 */new IFB_LLBINARY(99, "Primary account number, extended"),
			/* NAO USADO-035 */new IFB_LLNUM(99, "Track 2 data", false),
			/* NAO USADO-036 */new IFB_LLLNUM(104, "TRACK 3 DATA", true),
			/* 037 */new IF_CHAR(20, "RETRIEVAL REFERENCE NUMBER"),
			/* NAO USADO-038 */new IF_CHAR(6, "Approval code"),
			/* 039 */new IF_CHAR(2, "Action code"),
			/* NAO USADO-040 */new IFE_CHAR(3, "SERVICE RESTRICTION CODE"),
			/* 041 */new IF_CHAR(16, "Card acceptor terminal identification"),
			/* 042 */new IF_CHAR(15, "Card acceptor identification code"),
			/* NAO USADO-043 */new IFB_LLBINARY(99, "Card acceptor name/location"),
			/* NAO USADO-044 */new IFB_LLBINARY(99, "Additional response data"),
			/* NAO USADO-045 */new IFB_LLBINARY(99, "Track 1 data"),
			/* NAO USADO-046 */new IFB_LLBINARY(99, "ADITIONAL DATA - ISO"),
			/* 047 */new IF_CHAR(7, "Additional data - national"),
			/* 048 */new IFB_NUMERIC(1, "ADITIONAL DATA - PRIVATE", true),
			/* NAO USADO-049 */new IFB_NUMERIC(4, "Currency code, Transaction", true),
			/* NAO USADO-050 */new IFB_NUMERIC(3, "CURRENCY CODE, SETTLEMENT", true),
			/* NAO USADO-051 */new IFB_NUMERIC(3, "CURRENCY CODE, CARDHOLDER BILLING", true),
			/* NAO USADO-052 */new IFB_BINARY(8, "Personal identification number (PIN) data"),
			/* NAO USADO-053 */new IFB_BINARY(8, "Security related control information"),
			/* NAO USADO-054 */new IFB_LLLBINARY(999, "Amounts, additional"),
			/* NAO USADO-055 */new IFB_LLLBINARY(255, "IC card system related data"),
			/* NAO USADO-056 */new IFB_LLBINARY(99, "RESERVED ISO"),
			/* 057 */new IF_CHAR(4, "RESERVED NATIONAL"),
			/* 058 */new IFB_NUMERIC(4, "Reserved for national use", true),
			/* 059 */new IFB_NUMERIC(4, "Reserved for national use", true),
			/* 060 */new IFB_LLLBINARY(999, "Reserved for national use"),
			/* 061 */new IFB_LLLCHAR(100, "Reserved for national use"),
			/* 062 */new IFB_LLLCHAR(100, "Reserved for private use"),
			/* 063 */new IFB_LLLCHAR(999, "Reserved for national use"),
			/* NAO USADO-064 */new IFB_BINARY(8, "Message authentication code field")
	};

	public MyMeterPackager() {
		super();
		setFieldPackager(fld);
	}
}
