package com.credentek.msme.loginservice;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.credentek.msme.ApplicationGenParm.ApplicationGenParameter;
import com.credentek.msme.database.DaoBase;
import com.credentek.msme.utils.AesUtil;
import com.credentek.msme.utils.PathRead;

public class MakerCreationDaoImpl extends DaoBase implements MakerCreationDao {
	
	
	
	private static final Logger log = LogManager.getLogger(MakerCreationDaoImpl.class);
	public String SERVICE_TYPE = ApplicationGenParameter.SERVICE_TYPE;
	String environmentStr = PathRead.resourceBundleEnvi.getString("ENVIRONMENT");
//	AuditActivityForTransactionImpl auditCall = new AuditActivityForTransactionImpl();
	//AuditActivity activityBean = null;
	PathRead path = PathRead.getInstance();
	AesUtil aesUtil = new AesUtil();
	
	private static int capsCharArray[] = {65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90};
	private static int charArray[] = {97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122};
	private static int numArray[] = {48,49,50,51,52,53,54,55,56,57};
	private static int specCharArray[] = {33,64, 36,37, 38};
	private String timeOutToken = "d@dd@IPOHONE143WALI@qwertasd";
	
	private static ArrayList<Integer> capsCharArrayList = new ArrayList<Integer>();
	private static ArrayList<Integer> charArrayList = new ArrayList<Integer>();
	private static ArrayList<Integer> numArrayList = new ArrayList<Integer>();
	private static ArrayList<Integer> specCharArrayList = new ArrayList<Integer>();
	
	static
	{
		for(int i : capsCharArray)
		{
			capsCharArrayList.add(i);
		}
		
		for(int i : charArray)
		{
			charArrayList.add(i);
		}
		
		for(int i : specCharArray)
		{
			specCharArrayList.add(i);
		}
		
		for(int i : numArray)
		{
			numArrayList.add(i);
		}
	}
	
	
	
	
	public static boolean checkValidPassWord(String inputPass)
	{
//		int capsCharArray[] = {65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90};
//		int charArray[] = {97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122};
//		int numArray[] = {48,49,50,51,52,53,54,55,56,57};
//		int specCharArray[] = {33,64, 36,37, 38};
		
		boolean existflag = false;
		boolean capsCharExistFlag = false;
		boolean charExistFlag = false;
		boolean numExistFlag = false;
		boolean speCharExistFlag = false;
		
		
		
		char inputArr[] = inputPass.toCharArray();
		for(int i = 0 ;i < inputArr.length ; i++)
		{
			if(capsCharArrayList.contains((int)inputArr[i]))
			{
				capsCharExistFlag = true;
			}
			else if(charArrayList.contains((int)inputArr[i]))
			{
				charExistFlag = true;
			}
			else if(specCharArrayList.contains((int)inputArr[i]))
			{
				numExistFlag = true;
			}
			else if(numArrayList.contains((int)inputArr[i]))
			{
				speCharExistFlag = true;
			}
			
		}
		
		if(capsCharExistFlag && charExistFlag && numExistFlag && speCharExistFlag)
		{
			existflag = true;
		}
		return existflag;
	}
	
	

}
