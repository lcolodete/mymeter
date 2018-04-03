package org.mymeter.common;

import java.nio.ByteBuffer;
import java.util.Calendar;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

public class ISOFormatUtil {

	// ///////////////////////////////////////////////////////////////////
	//
	// Header
	//
	// ///////////////////////////////////////////////////////////////////

	public static void formatHeaderSwapDirection(ISOMsg msg) throws ISOException {
		
		byte[] header = msg.getHeader();
		
		byte[] tpduHeader = new byte[5];
		tpduHeader[0] = (byte) 0x60;
		
		tpduHeader[1] = header[3];
		tpduHeader[2] = header[4];
		
		tpduHeader[3] = header[1];
		tpduHeader[4] = header[2];
		
		msg.setHeader(tpduHeader);
	}

	public static void formatHeaderWithDestinationAddress(String destinationAddress, ISOMsg msg) throws ISOException {
		byte[] tpduHeader = new byte[5];
		tpduHeader[0] = (byte) 0x60;
		
		byte[] byteNii = ISOUtil.hex2byte(destinationAddress);
		
		//Seta NII destino
		tpduHeader[1] = byteNii[0];
		tpduHeader[2] = byteNii[1];
		
		//Seta NII origem fixo 0000
		tpduHeader[3] = (byte) 0x00;
		tpduHeader[4] = (byte) 0x00;
		
		msg.setHeader(tpduHeader);
	}
	
	public static void formatHeader(String destinationNii, String sourceNii, ISOMsg msg) throws ISOException {
		byte[] tpduHeader = new byte[5];
		tpduHeader[0] = (byte) 0x60;

		byte[] byteNii = ISOUtil.hex2byte(destinationNii);
		
		//Seta NII destino
		tpduHeader[1] = byteNii[0];
		tpduHeader[2] = byteNii[1];

		byteNii = ISOUtil.hex2byte(sourceNii);
		
		//Seta NII origem
		tpduHeader[3] = byteNii[0];
		tpduHeader[4] = byteNii[1];

		msg.setHeader(tpduHeader);
	}

	/////////////////////////////////////////////////////////////////////
	//
	// Campo 12
	//
	/////////////////////////////////////////////////////////////////////
	
	public static String formatField12(Calendar c) throws ISOException {
		StringBuilder f12Builder = new StringBuilder();
		f12Builder.append(ISOUtil.padleft(c.get(Calendar.HOUR_OF_DAY)+"", 2, '0'))
				  .append(ISOUtil.padleft(c.get(Calendar.MINUTE)+"", 2, '0'))
				  .append(ISOUtil.padleft(c.get(Calendar.SECOND)+"", 2, '0'));
		
		return f12Builder.toString();
	}
	
	/////////////////////////////////////////////////////////////////////
	//
	// Campo 13
	//
	/////////////////////////////////////////////////////////////////////
	
	public static String formatField13(Calendar c) throws ISOException {
		int month = c.get(Calendar.MONTH) + 1; //adiciona 1 porque é zero-based
		
		StringBuilder f13Builder = new StringBuilder();
		f13Builder.append(ISOUtil.padleft(month+"", 2, '0'))
				  .append(ISOUtil.padleft(c.get(Calendar.DAY_OF_MONTH)+"", 2, '0'));
		
		return f13Builder.toString();
	}
	
	/////////////////////////////////////////////////////////////////////
	//
	// Campo 35
	//
	/////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @deprecated Porque o empacotador do campo 35 mudou para IFB_LLNUM
	 * @param valueField35
	 * @return
	 * @throws ISOException
	 */
	public static byte[] formatField35(String valueField35) throws ISOException {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		bb.put(ISOUtil.hex2byte(valueField35));
		bb.flip();
		
		byte[] sf35 = new byte[bb.limit()];
		bb.get(sf35, 0, bb.limit());
		return sf35;
	}
	
	/////////////////////////////////////////////////////////////////////
	//
	// Campo 48
	//
	/////////////////////////////////////////////////////////////////////
	
	public static byte[] formatField48(String valueField48) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		bb.put(ISOUtil.hex2byte(valueField48));
		bb.flip();
		
		byte[] sf48 = new byte[bb.limit()];
		bb.get(sf48, 0, bb.limit());
		return sf48;
	}
	
	/////////////////////////////////////////////////////////////////////
	//
	// Campo 52
	//
	/////////////////////////////////////////////////////////////////////
	
	public static byte[] formatField52(String valueField52) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		bb.put(ISOUtil.hex2byte(valueField52));
		bb.flip();
		
		byte[] sf52 = new byte[bb.limit()];
		bb.get(sf52, 0, bb.limit());
		return sf52;
	}
	
	/////////////////////////////////////////////////////////////////////
	//
	// Campo 53
	//
	/////////////////////////////////////////////////////////////////////
	
	public static byte[] formatField53(String valueField53) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		bb.put(ISOUtil.hex2byte(valueField53));
		bb.flip();
		
		byte[] sf53 = new byte[bb.limit()];
		bb.get(sf53, 0, bb.limit());
		return sf53;
	}
	
	/////////////////////////////////////////////////////////////////////
	//
	// Campo 55
	//
	/////////////////////////////////////////////////////////////////////
	
	public static byte[] formatField55(String valueField55) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		bb.put(ISOUtil.hex2byte(valueField55));
		bb.flip();
		
		byte[] sf55 = new byte[bb.limit()];
		bb.get(sf55, 0, bb.limit());
		return sf55;
	}
	
	//////////////////////////////////////////////////////////////////////
	//
	// Campo 60
	//
	//////////////////////////////////////////////////////////////////////

	public static byte[] formatField60_client(String fieldValue) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		bb.put(fieldValue.getBytes());
		bb.flip();
		byte[] fieldBytes = new byte[bb.limit()];
		bb.get(fieldBytes, 0, bb.limit());
		
		return fieldBytes;
	}
	
	public static byte[] formatField60_server(String fieldValue) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		bb.put(ISOUtil.hex2byte(fieldValue));
		bb.flip();
		byte[] fieldBytes = new byte[bb.limit()];
		bb.get(fieldBytes, 0, bb.limit());

		return fieldBytes;
	}

	//////////////////////////////////////////////////////////////////////
	//
	// Campo 61
	//
	//////////////////////////////////////////////////////////////////////
	
	public static byte[] formatField61(String valueField61) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		bb.put(ISOUtil.hex2byte(valueField61));
		bb.flip();
		
		byte[] sf61 = new byte[bb.limit()];
		bb.get(sf61, 0, bb.limit());
		return sf61;
	}

	public static byte[] formatField61_16(String str) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		
	    //Ex: 01 81119724FFFFFFFF 09 303330303430343736
	    String codigoOperadora = str.substring(0, 2);//1 BCD
	    String numCelular = str.substring(2, 18);//8 BIN
	    String tamanhoNumSerieSimcard = str.substring(18, 20);//1 BCD
	    
	    int tam = 0;
	    String numSerieSimcard;
	    try 
	    {
	    	tam = Integer.parseInt(tamanhoNumSerieSimcard);
	    	numSerieSimcard = str.substring(20, 20 + tam*2);
	    }  
	    catch (Exception e) 
	    {    	
	    	numSerieSimcard = str.substring(20);
		}
		
	    bb.put(ISOUtil.str2bcd(codigoOperadora, true));//BCD
	    bb.put(ISOUtil.hex2byte(numCelular));//BIN
	    bb.put(ISOUtil.str2bcd(tamanhoNumSerieSimcard, true));//BCD
	    bb.put(ISOUtil.hex2byte(numSerieSimcard));//ASC

	    bb.flip();
		byte[] sf6 = new byte[bb.limit()];
		bb.get(sf6, 0, bb.limit());
		
		return sf6;
	}

	//////////////////////////////////////////////////////////////////////
	//
	// Campo 63
	//
	//////////////////////////////////////////////////////////////////////
	
	public static byte[] formatField63(String fieldValue) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		bb.put(fieldValue.getBytes());
		bb.flip();
		byte[] fieldBytes = new byte[bb.limit()];
		bb.get(fieldBytes, 0, bb.limit());
		
		return fieldBytes;
	}
	
	public static byte[] formatField63ForAutoProcedures(String fieldValue) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		
		//os 4 primeiros digitos sao o tamanho (em bytes) do que vem a seguir
		String internalSize = fieldValue.substring(0, 4);
		String internalValue = fieldValue.substring(4);
		
		bb.put(ISOUtil.str2bcd(internalSize, true));//BCD
		bb.put(ISOUtil.hex2byte(internalValue));//ASC
		bb.flip();
		byte[] fieldBytes = new byte[bb.limit()];
		bb.get(fieldBytes, 0, bb.limit());
		
		return fieldBytes;
	}

	//////////////////////////////////////////////////////////////////////
	//
	// Campo 64
	//
	//////////////////////////////////////////////////////////////////////

	public static byte[] formatField64(String valueField64) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		bb.put(ISOUtil.hex2byte(valueField64));
		bb.flip();
		
		byte[] sf64 = new byte[bb.limit()];
		bb.get(sf64, 0, bb.limit());
		return sf64;
	}

}
