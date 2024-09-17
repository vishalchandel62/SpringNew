package com.credentek.msme.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Crypt {
	

	private static final Logger log = LogManager.getLogger(Crypt.class);
	String m_cmd;
	
	/*private static final String public_key = "-----BEGIN PUBLIC KEY-----\n"+
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVd/gb2ORdLI7nTRHJR8C5EHs4\n" +
			"RkRBcQuQdHkZ6eq0xnV2f0hkWC8h0mYH/bmelb5ribwulMwzFkuktXoufqzoft6Q\n"+
			"6jLQRnkNJGRP6yA4bXqXfKYj1yeMusIPyIb3CTJT/gfZ40oli6szwu4DoFs66IZp\n" +
			"JLv4qxU9hqu6NtJ+8QIDAQAB\n" +
			"-----END PUBLIC KEY-----";*/
	
	private static final String public_key = "-----BEGIN PUBLIC KEY-----"+
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVd/gb2ORdLI7nTRHJR8C5EHs4" +
			"RkRBcQuQdHkZ6eq0xnV2f0hkWC8h0mYH/bmelb5ribwulMwzFkuktXoufqzoft6Q"+
			"6jLQRnkNJGRP6yA4bXqXfKYj1yeMusIPyIb3CTJT/gfZ40oli6szwu4DoFs66IZp" +
			"JLv4qxU9hqu6NtJ+8QIDAQAB" +
			"-----END PUBLIC KEY-----";
	
	private static final String key = "MIICXQIBAAKBgQDVd/gb2ORdLI7nTRHJR8C5EHs4RkRBcQuQdHkZ6eq0xnV2f0hk"+
			"WC8h0mYH/bmelb5ribwulMwzFkuktXoufqzoft6Q6jLQRnkNJGRP6yA4bXqXfKYj"+
			"1yeMusIPyIb3CTJT/gfZ40oli6szwu4DoFs66IZpJLv4qxU9hqu6NtJ+8QIDAQAB"+
			"AoGADbnXFENP+8W/spO8Dws0EzJCGg46mVKhgbpbhxUJaHJSXzoz92/MKAqVUPI5"+
			"mz7ZraR/mycqMia+2mpo3tB6YaKiOpjf9J6j+VGGO5sfRY/5VNGVEQ+JLiV0pUmM"+
			"doq8n2ZhKdSd5hZ4ulb4MFygzV4bmH29aIMvogMqx2Gkp3kCQQDx0UvBoNByr5hO"+
			"Rl0WmDiDMdWa9IkKD+EkUItR1XjpsfEQcwXet/3QlAqYf+FE/LBcnA79NdBGxoyJ"+
			"XS+O/p4rAkEA4f0JMSnIgjl7Tm3TpNmbHb7tsAHggWIrPstCuHCbNclmROfMvcDE"+
			"r560i1rbOtuvq5F/3BQs+QOnOIz1jLslUwJAbyEGNZfX87yqu94uTYHrBq/SQIH8"+
			"sHkXuH6jaBo4lP1HkY2qtu3LYR2HuQmb1v5hdk3pvYgLjVsVntMKVibBPQJBAKd2"+
			"Dj20LLTzS4BOuirKZbuhJBjtCyRVTp51mLd8Gke9Ol+NNZbXJejNvhQV+6ad7ItC"+
			"gnDfMoRERMIPElZ6x6kCQQCP45DVojZduLRuhJtzBkQXJ4pCsGC8mrHXF3M+hJV+"+
			"+LAYJbXrQa4mre59wR0skgb6CwGg1siMrDzJgu3lmBB0";
	
	public String GeneratePassPhrase(long p_passPhraseLen, long reqID, String path)
	{
		String p_passPhrase="";
		String randPwFile = "randPwFile"; 
		String agntID = "0";
		log.info("Start of the p_passPhrase method");
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter( new FileWriter(path+reqID+randPwFile));
			//writer.write("snprintf(m_msg,MAX_MSG_LEN,â€�openssl rand %sâ€�);");
			writer.write("77777777777777777777777777777777777777777777777777;");
		}
		catch ( IOException e)
		{
			log.info("IOException:",e);
		}
		finally
		{
			try
			{
				if ( writer != null)
					writer.close( );
			}
			catch ( IOException e)
			{
				log.info("IOException:",e);
			}
	     }
		//Password is null then execute command : openssl rand 100 -out pwdfile 
		//m_cmd = "openssl rand -base64 "+p_passPhraseLen+" -out "+(path+reqID+randPwFile);
		//log.info("m_cmd::"+m_cmd);
		//p_passPhrase = ExecuteCryptCommand(m_cmd,"genratePass",agntID,reqID,path);
		
		//p_passPhrase = "snprintf(m_msg,MAX_MSG_LEN,â€�openssl rand %sâ€�);";
		p_passPhrase =   "77777777777777777777777777777777777777777777777777"; //NO OF 7 is 50
		log.info("End of the p_passPhrase method");

		return p_passPhrase;
	}
	
	//public long EncryptPassPhrase(String p_publicKeyFileName, String p_passPhrase, long p_passPhraseLen, String p_encPassPhrase)
	
	public String EncryptPassPhrase(String p_publicKeyFileName, String p_passPhrase, long p_passPhraseLen,String dbPublicKey,String agentID,long reqID, String path)
	{
		log.info("Start of the encryptPassPhrase method");
		String l_encKey="";
		long l_encKeyLen = 0;
		File fp2,fp1;
		String randPwFile = "randPwFile";
		String hexString = "";
		//PathRead pathReader = new PathRead();
		//Password is null then execute command : openssl rand 100 -out pwdfile else create pwdfile with se
			
		
			//Create New file agentId.pub containing public key from db
			try
			{
				FileOutputStream fop = null;
			    fp2=new File(path+p_publicKeyFileName);	
					
		    	boolean delFlag = fp2.delete();
		    	boolean newFlag = fp2.createNewFile();
		    	//log.info("File delete successfully :"+delFlag+ "New file created: "+newFlag);
			   
		    
				fop = new FileOutputStream(fp2);
			    byte[] contentInBytes = dbPublicKey.getBytes();
				fop.write(contentInBytes);
				fop.flush();
				fop.close();
			}
			catch (Exception e) {
				//e.printStackTrace();
				log.info("Exception:",e);
			}
			
			
    // Step 2 : encrypt this key using public key of agent 1
	//For POC/For Live Server
		//m_cmd = ""+pathReader.getPath("opensslpath")+"openssl rsautl -encrypt -pkcs -inkey  "+(path+p_publicKeyFileName)+" -pubin -in "+(path+reqID+randPwFile)+" -out "+path+reqID+agentID+"encKeyTemp.key";
		
    //FOR UAT/Credentek
	m_cmd = "openssl rsautl -encrypt -pkcs -inkey  "+(path+p_publicKeyFileName)+" -pubin -in "+(path+reqID+randPwFile)+" -out "+path+reqID+agentID+"encKeyTemp.key";
		
		try 
		{
			  //To execute encrypt command in hex format
			  hexString = ExecuteCryptCommand(m_cmd,"EncryptPass",agentID,reqID,path);
			  
			  log.info("End of the executeCryptCommand method"); 
			  log.info("File write completed");
			  
			  if(hexString.equals("error"))
			  {
				  return hexString;
			  }
			 
			 
		} catch (Exception e) {
			log.info("Exception:",e);
		} 
		 
		log.info("End of the encryptPassPhrase method"); 
		return hexString;
	}
	
	
	
	
	/*convert string to hex */
	public static String stringToHex(byte bytes[]) { 
		
		StringBuilder sb = new StringBuilder(bytes.length * 2);  
		Formatter formatter = new Formatter(sb);  
		 
		
		
		for (byte b : bytes) 
		{  
		        formatter.format("%08X", b);  
		}  
			 
		    
		    return sb.toString();
	  }
	
	@SuppressWarnings("finally")
	private String ExecuteCryptCommand(String p_cmd,String check,String agntID,long reqID,String path)
	{
	     String processOutput = ""; 
	     Process p;
	     log.info("Start of the executeCryptCommand method");
		try {
				
				p = Runtime.getRuntime().exec(p_cmd);
				int w = p.waitFor(); 
				//To generate Encrypt password 
				if(check.equals("EncryptPass"))
				{
					// Check if we can decrypt the message to get original file
					  File file = new File(path+reqID+agntID+"encKeyTemp.key");
					  FileInputStream fin = null;
					  try
					  {
						  fin = new FileInputStream(file);
						  byte fileContent[] = new byte[(int)file.length()];
						  
						  /*
					        * To read content of the file in byte array, use
					        * int read(byte[] byteArray) method of java FileInputStream class.
					        *
					        */
						   fin.read(fileContent);
					       //create string from byte array
					       String strFileContent = stringToHex(fileContent);
					       processOutput = strFileContent;
					       fin.close();
					  }
					  catch(FileNotFoundException e)
					  {
					      log.info("File not found" , e);
					      processOutput = "";
					      return processOutput; 
					  }
					  catch(IOException ioe)
					  {
					      log.info("Exception while reading the file " , ioe);
					      processOutput = "";
					      
					  }
					  finally
					  {
						  if(fin!=null){
							  fin.close();  
						  }
						  
						  return processOutput; 
					  }
				}
				else if(check.equals("decryptText"))
				{
					log.info("In decrypt::");
					//Check if we can decrypt the message to get original file
					  File file = new File(path+agntID+"_decrypt.txt");
					  FileInputStream fin = null;
					  try
					  {
						  fin = new FileInputStream(file);
						  byte fileContent[] = new byte[(int)file.length()];
						  
						  /*
					        * To read content of the file in byte array, use
					        * integer read(byte[] byteArray) method of java FileInputStream class.
					        *
					        */
						   fin.read(fileContent);
					       //create string from byte array
					       String strFileContent = new String(fileContent);
					       processOutput = strFileContent;
					       fin.close();
					       
					       log.info("process op :"+processOutput);
					  }
					  catch(FileNotFoundException e)
					  {
					      log.info("File not found" , e);
					      processOutput = "";
					      return processOutput; 
					  }
					  catch(IOException ioe)
					  {
					      log.info("Exception while reading the file " , ioe);
					      processOutput = "";
					      
					  }
					  finally
					  {
						  if(fin!=null){
							  fin.close();  
						  }
						  return processOutput; 
					  }
					  
				}
				else
				{
					//Read password file 
					File file = new File(path+reqID+"randPwFile");
					FileInputStream fin = null;
					  try
					  {
						  fin = new FileInputStream(file);
						  byte fileContent[] = new byte[(int)file.length()];
						  
						   /*
					        * To read content of the file in byte array, use
					        * int read(byte[] byteArray) method of java FileInputStream class.
					        *
					        */
					       fin.read(fileContent);
					       //create string from byte array
					       String strFileContent = stringToHex(fileContent);
					       processOutput = strFileContent;
					       fin.close();
					  }
					  catch(FileNotFoundException e)
					  {
					      log.info("File not found" , e);
					      processOutput = "";
					      
					  }
					  catch(IOException ioe)
					  {
					      log.info("Exception while reading the file " , ioe);
					      processOutput = "";
					     
					  }
					  finally
					  {
						  if(fin!=null){
							  fin.close();  
						  }
						  return processOutput; 
					  }
				}
		} catch (IOException e) {
			//e.printStackTrace();
			log.info("IOException:",e);
			processOutput = "";
			return processOutput; 
		} catch (InterruptedException e) {
			//e.printStackTrace();
			log.info("InterruptedException:",e);
			processOutput = "";
			return processOutput; 
		} 
		finally
		  {
			  return processOutput; 
		  }
	     
	}
	public String decryptText(String p_key, String p_data ,String sessionID, String path)
	{
		String l_encKey="";
		long l_encKeyLen = 0;
		File fp2,fp1,fp3;
		String randPwFile = "randPwFile";
		String hexString = "";
		String strFileContent = "";
		//PathRead pathReader = new PathRead();
		
		//For POC & Live
		//String opensslpath = pathReader.getPath("opensslpath")+"openssl ";
		
		
		//For Credentek & UAT
		String opensslpath = "openssl ";
			
		log.info("Start of the decrypt method");
			//Create New file agentId.pub containing public key from db
		String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n"+bitCutStr(p_key)+"-----END RSA PRIVATE KEY-----";//bitCutStr(p_key); 
		try
			{
				FileOutputStream fop = null;
			    fp2=new File(path+"privatekey.key");	
			    if(!fp2.exists())
			    {
			    	boolean newFlag = fp2.createNewFile();
			    }

				fop = new FileOutputStream(fp2);
			    byte[] contentInBytes = privateKey.getBytes();//dbPublicKey.getBytes();
				fop.write(contentInBytes);
				fop.flush();
				fop.close();
			}
			catch (Exception e) {
				//e.printStackTrace();
				log.info("Exception:",e);
			}
			try
			{
				FileOutputStream fop = null;
			    fp1=new File(path+sessionID+"_crypted_64.txt");	
					
		    	boolean delFlag = fp1.delete();
		    	boolean newFlag = fp1.createNewFile();
		    	
				fop = new FileOutputStream(fp1);
			    byte[] contentInBytes = p_data.getBytes();//dbPublicKey.getBytes();
				fop.write(contentInBytes);
				fop.flush();
				fop.close();
			}
			catch (Exception e) {
				//e.printStackTrace();
				log.info("Exception:",e);
			}
			
		     // Step 2 : encrypt this key using public key of agent 1
			
			m_cmd = opensslpath+"base64 -d -in "+(path+sessionID+"_crypted_64.txt")+" -out "+path+sessionID+"_decrypt.txt";
			Process p=null;
			
			try {
					p = Runtime.getRuntime().exec(m_cmd);
					int w = p.waitFor(); 
					
				} catch (IOException e) {
					//e.printStackTrace();
					log.info("Exception:",e);
				} catch (InterruptedException e) {
					//e.printStackTrace();
					log.info("Exception:",e);
				}
				finally{
					if(p!=null){
						p.destroy();	
					}
				 
				}
				
				m_cmd =  opensslpath+"rsautl -pkcs -inkey "+(path+"privatekey.key")+" -decrypt -in "+path+sessionID+"_decrypt.txt -out "+path+sessionID+"_decrypt64.txt";
				try {
						p = Runtime.getRuntime().exec(m_cmd);
						int w = p.waitFor(); 
						
						File file = new File(path+sessionID+"_decrypt64.txt");
						FileInputStream fin = null;
						try
						{
							  fin = new FileInputStream(file);
							  byte fileContent[] = new byte[(int)file.length()];
							  
							  /*
						        * To read content of the file in byte array, use
						        * int read(byte[] byteArray) method of java FileInputStream class.
						        *
						        */
							   fin.read(fileContent);
						       //create string from byte array
						       strFileContent = new String(fileContent);
						       String string4 = bitCutStr(strFileContent);
						       //processOutput = strFileContent;
						       
						       
						       boolean delFlag = file.delete();
						       boolean newFlag = file.createNewFile();
							   
						    
						    	FileOutputStream fop = new FileOutputStream(file);
							    byte[] contentInBytes = string4.getBytes();//dbPublicKey.getBytes();
								fop.write(contentInBytes);
								fop.flush();
								fop.close();
						}
						catch(FileNotFoundException e)
						{
						      log.info("File not found" , e);
						      strFileContent = "";
						      return strFileContent; 
						}
						catch(IOException ioe)
						{
						      log.info("Exception while reading the file " , ioe);
						      strFileContent = "";
						      
						}
						finally
						{
						  fin.close();
						}
					} catch (IOException e) {
						//e.printStackTrace();
						log.info("IOException:",e);
					} catch (InterruptedException e) {
						//e.printStackTrace();
						log.info("InterruptedException:",e);
					}
					finally{
						if(p!=null){
							p.destroy();	
						}
						
					}
					
					m_cmd = opensslpath+"base64 -d -in "+(path+sessionID+"_decrypt64.txt")+" -out "+path+sessionID+"_decStr.txt";
					
					String finalString= "";
					
					try
					{
						FileInputStream fin = null;
						File file = new File(path+sessionID+"_decStr.txt");
					
						try 
						{
						  
						  	p = Runtime.getRuntime().exec(m_cmd);
							int w = p.waitFor(); 
							
							fin = new FileInputStream(file);
							byte fileContent[] = new byte[(int)file.length()];
							  
							/*
					        * To read content of the file in byte array, use
					        * int read(byte[] byteArray) method of java FileInputStream class.
					        *
					        */
						    fin.read(fileContent);
					        //create string from byte array
						    finalString = new String(fileContent);


						} catch (Exception e) {
							log.info("Exception:",e);
						} 
						finally
						  {
							if(p!=null){
									p.destroy();	
								}
							if(fin!=null){
								fin.close(); 
							}
							  
						  }
					}
					catch (Exception e) {
						log.info("In decrypttext method ",e);
					}
					try
						{
						    File fp=new File(path+sessionID+"_crypted_64.txt");	
					    	boolean delFlag = fp.delete();
					    	
					    	fp=new File(path+sessionID+"_decrypt.txt");	
					    	delFlag = fp.delete();
					    	
					    	fp=new File(path+sessionID+"_decrypt64.txt");	
					    	delFlag = fp.delete();
					    	
					    	fp=new File(path+sessionID+"_decStr.txt");	
					    	delFlag = fp.delete();
					    	
						}
						catch (Exception e) {
							log.info("Exception:",e);
						}
					log.info("End of the decrypttext method"); 
					return finalString;
	}
	
	public String encryptText(String p_key, String p_data ,String sessionID,String path)
	{
		String l_encKey="";
		long l_encKeyLen = 0;
		File fp2,fp1,fp3;
		String randPwFile = "randPwFile";
		String hexString = "";
		String strFileContent = "";
		//PathRead pathReader = new PathRead();
		
		//For POC & Live
		//String opensslpath = pathReader.getPath("opensslpath")+"openssl ";
		//For Credentek & UAT
		
		String opensslpath = "openssl ";
			
		log.info("Start of the encrypt method");
			//Create New file agentId.pub containing public key from db
		String publicKey = "-----BEGIN RSA PRIVATE KEY-----\n"+bitCutStr(p_key)+"-----END RSA PRIVATE KEY-----";//bitCutStr(p_key); 
		log.info("Public Key :");
		//System.out.println("Public Key :"+publicKey);
		
		//System.out.println(">>>>>>>>>>>>"+path+"publickey.key");
		try
			{
				FileOutputStream fop = null;
			    fp2=new File(path+"publickey.key");	
			    if(!fp2.exists())
			    {
			    	boolean newFlag = fp2.createNewFile();
			    }

				fop = new FileOutputStream(fp2);
			    byte[] contentInBytes = publicKey.getBytes();//dbPublicKey.getBytes();
				fop.write(contentInBytes);
				fop.flush();
				fop.close();
			}
			catch (Exception e) {
				log.info("Exception:",e);
			}
			try
			{
				FileOutputStream fop = null;
			    fp1=new File(path+sessionID+"_crypted_64.txt");	
					
		    	boolean delFlag = fp1.delete();
		    	boolean newFlag = fp1.createNewFile();
		    	
				fop = new FileOutputStream(fp1);
			    byte[] contentInBytes = p_data.getBytes();//dbPublicKey.getBytes();
				fop.write(contentInBytes);
				fop.flush();
				fop.close();
			}
			catch (Exception e) {
				log.info("Exception:",e);
			}
			
		     // Step 2 : encrypt this key using public key of agent 1
			
			m_cmd = opensslpath+"base64 -d -in "+(path+sessionID+"_crypted_64.txt")+" -out "+path+sessionID+"_encrypt.txt";
			Process p=null;
			
			try {
					p = Runtime.getRuntime().exec(m_cmd);
					int w = p.waitFor(); 
					
				} catch (IOException e) {
					log.info("IOException:",e);
				} catch (InterruptedException e) {
					log.info("InterruptedException:",e);
				}
				finally{
					if(p!=null){
						p.destroy();	
					}
					
				}
				//         openssl rsautl -decrypt -inkey privatekey -in encrtyptedFile -out decryptedFile
				//m_cmd =  opensslpath+"rsautl -pkcs -inkey "+(path+"privatekey.key")+" -decrypt -in "+path+sessionID+"_decrypt.txt -out "+path+sessionID+"_decrypt64.txt";
				//openssl   rsautl     -encrypt    -inkey     publicKey     -pubin     -in     decryptedFile   -out    encrtyptedFile
				m_cmd =  opensslpath+"rsautl -encrypt -inkey "+(path+"publickey.key")+" -pubin -in "+path+sessionID+"_encrypt.txt -out "+path+sessionID+"_encrypt64.txt";
				//m_cmd =  opensslpath+"rsautl -pkcs -inkey "+(path+"privatekey.key")+" -encrypt -in "+path+sessionID+"_encrypt.txt -out "+path+sessionID+"_encrypt64.txt";
				try {
						p = Runtime.getRuntime().exec(m_cmd);
						int w = p.waitFor(); 
						
						File file = new File(path+sessionID+"_encrypt64.txt");
						FileInputStream fin = null;
						try
						{
							  fin = new FileInputStream(file);
							  byte fileContent[] = new byte[(int)file.length()];
							  
							
							   fin.read(fileContent);
						       //create string from byte array
						       strFileContent = new String(fileContent);
						       String string4 = bitCutStr(strFileContent);
						       
						       boolean delFlag = file.delete();
						       boolean newFlag = file.createNewFile();
							   
						    
						    	FileOutputStream fop = new FileOutputStream(file);
							    byte[] contentInBytes = string4.getBytes();//dbPublicKey.getBytes();
								fop.write(contentInBytes);
								fop.flush();
								fop.close();
						}
						catch(FileNotFoundException e)
						{
						      log.info("File not found" , e);
						      //System.out.println("File not found" + e);
						      strFileContent = "";
						      return strFileContent; 
						}
						catch(IOException ioe)
						{
						      log.info("Exception while reading the file " , ioe);
						      //System.out.println("Exception while reading the file " + ioe);
						      strFileContent = "";
						      
						}
						finally
						{
						  fin.close();
						}
					} catch (IOException e) {
						//e.printStackTrace();
						log.info("IOException",e);
					} catch (InterruptedException e) {
						//e.printStackTrace();
						log.info("InterruptedException",e);
					}
					finally{
						p.destroy();
					}
					
					m_cmd = opensslpath+"base64 -d -in "+(path+sessionID+"_encrypt64.txt")+" -out "+path+sessionID+"_encStr.txt";
					
					String finalString= "";
					
					try
					{
						FileInputStream fin = null;
						File file = new File(path+sessionID+"_decStr.txt");
					
						try 
						{
						  
						  	p = Runtime.getRuntime().exec(m_cmd);
							int w = p.waitFor(); 
							
							fin = new FileInputStream(file);
							byte fileContent[] = new byte[(int)file.length()];
							  
							/*
					        * To read content of the file in byte array, use
					        * int read(byte[] byteArray) method of java FileInputStream class.
					        *
					        */
						    fin.read(fileContent);
					        //create string from byte array
						    finalString = new String(fileContent);
							  

						} catch (Exception e) {
							log.info("Exception:",e);
						} 
						finally
						  {
							  p.destroy();
							  fin.close();
						  }
					}
					catch (Exception e) {
						log.info("In encrypttext method ",e);
						//System.out.println("In encrypttext method "+e);
					}
					try
						{
						    File fp=new File(path+sessionID+"_crypted_64.txt");	
					    	boolean delFlag = fp.delete();
					    	
					    	fp=new File(path+sessionID+"_encrypt.txt");	
					    	delFlag = fp.delete();
					    	
					    	fp=new File(path+sessionID+"_encrypt64.txt");	
					    	delFlag = fp.delete();
					    	
					    	fp=new File(path+sessionID+"_encStr.txt");	
					    	delFlag = fp.delete();
					    	
						}
						catch (Exception e) {
							log.info("Exception:",e);
						}
					log.info("End of the encrypt text method"); 
					return finalString;
	}
	
	public static String bitCutStr(String str)
	{
		int  j = 0;
		String encryptedKey=str;
		String encTemp = "";

		String temp = encryptedKey;
		int enclength = encryptedKey.length();
		
		while(true)	
		{
			j=temp.length();
			if(j>64)
			{
				encTemp=encTemp+temp.substring(0,64)+"\n";
			}
			else
			{
				encTemp=encTemp+temp.substring(0,j)+"\n";
			}	
			
			j=temp.length();
			
			if(j>64)
			{
				temp = temp.substring(64);
			}
			if(j<=64)
				break;
		}
		return encTemp;
	}
	
	// SessionID, Path variable, Secret Key String, Bytes to Encrypt
	public static int EncryptStringDES(String sessionID, String path, String randomKey,String data, StringBuffer encrypted_parameter)
	{
		String l_encKey="";
		long l_encKeyLen = 0;
		File fp1, fp2;
		//String randPwFile = "randPwFile";
		String publicKey = path + "pub.key";
		try
		{
			FileOutputStream fop = null;			
			
			//create randomKeyFile
			String randomKeyFile = path+"randomKey"+sessionID;
			fp1 = new File(randomKeyFile);
			// delete existing file
			if(fp1.exists())
			{
				boolean flag = fp1.delete();
			}
			boolean flag = fp1.createNewFile();
			fp1 = new File(randomKeyFile);
			fop = new FileOutputStream(fp1);
			
	        // Write randomKey
	        fop.write(randomKey.getBytes());
			fop.flush();
			fop.close();
			
			//create dataToEncryptFile
			String dataToEncryptFile = path+"data"+sessionID;
			fp2 =  new File(dataToEncryptFile);
			
			//delete existing file
			if(fp2.exists())
			{
				flag = fp2.delete();				
			}
			
	  		  flag = fp2.createNewFile();
			  fp2 =  new File(dataToEncryptFile);
			  fop = new FileOutputStream(fp2);
			  log.info("file created successfully ::: "+flag);
			  					
			  fop.write(data.getBytes());
			  fop.flush();
			  fop.close();
			
			  // Write dataToEncrypt
			  String encryptedFile = path+"encryptedFile"+sessionID;			
			  fp1 = new File(encryptedFile);
			
			  if(fp1.exists())
			  {
				  flag = fp1.delete();
			  }
			
			  flag = fp1.createNewFile();
			
			
			  log.info("Start of the EncryptedPassPhrase method");
			  
			  String m_cmd = "openssl rsautl -encrypt -inkey "+ publicKey + " -pubin -in "+ dataToEncryptFile + " -out "+ encryptedFile; 
			
			  Process p = null; 
			 
			  try 
			  {
				  //To execute encrypt command in hex format
				  log.info("Start of the ExecuteCryptCommand Method");
				  p = Runtime.getRuntime().exec(m_cmd);
				  int w = p.waitFor();
				  //hexString = ExecuteCryptCommand(m_cmd,"EncryptPass",agentID,reqID,path);
				  log.info("End of the executeCryptCommand method"); 
				  log.info("File write completed");
				 
			  } catch (IOException e) {
				  //e.printStackTrace();
				  log.info("EncryptStringDES : " ,e);
			  } catch (InterruptedException e) {
				  log.info("InterruptedException in encryption : " ,e);
				  //e.printStackTrace();
			  }finally{
					p.destroy();
			  }
			  		  
				
			  try
			  {
				  FileInputStream fip = null;
				  fp2=new File(encryptedFile);
				  if (fp2 == null)
				  {
					  return 0;
				  }
				    
				
				  StringBuilder sb = new StringBuilder();
				  FileInputStream fileInputStream=null;
				  
			        File file = new File(encryptedFile);
			 
			        byte[] bFile = new byte[(int) file.length()];
			 
			        try {
			            //convert file into array of bytes
				    fileInputStream = new FileInputStream(file);
				    fileInputStream.read(bFile);
				    fileInputStream.close();
				    //System.out.println("Done");
			       
		        }catch(Exception e){
		        	//e.printStackTrace();
		        	log.info("Exception:",e);
		        }
				    //sb.append(Base64.encodeBase64(bFile));
			        sb.append(bFile);
					
				  log.info("Encrypted file content:" + sb);
				  encrypted_parameter.append(sb.toString());
				  
				  return 1;					
			  }
			  catch (Exception e) {
				  //e.printStackTrace();
				  log.info("Exception:",e);
			  }finally{
				  try
				  {
					File fp=new File(randomKeyFile);	
					  boolean delFlag = fp.delete();
				    	
					  fp=new File(dataToEncryptFile);	
					  delFlag = fp.delete();
				    	
					  fp=new File(encryptedFile);	
					  delFlag = fp.delete();
				    	
				  }
				  catch (Exception e) {
					  log.info("Exception:",e);
				  }
			  }	
			}
			catch (Exception e) {
				log.info("Exception:",e);
			}
			return 0;
		}
	// SessionID, Path variable, secret Key String, Bytes to decrypt
	public static int DecryptStringDES(String sessionID, String path, String randomKey,String data, StringBuffer messageFromServerDecrypted)
		{
			try
				{
					String privateKey = path + "private.key";
					long l_encKeyLen = 0;
					File fp2,fp1;
					//String randPwFile = "randPwFile";
					FileOutputStream fop = null;
					//create randomKeyFile
					String randomKeyFile = path+"randomKey"+sessionID;
					fp1 = new File(randomKeyFile);
					// Delete existing file
					if(fp1.exists())
					{
						boolean flag = fp1.delete();
					}
					boolean flag = fp1.createNewFile();
					fp1 = new File(randomKeyFile);
					fop = new FileOutputStream(fp1);
					log.info("file created successfully ::: "+flag);
					//Write randomKey
					fop.write(randomKey.getBytes());
					fop.flush();
					fop.close();
					
					
					//create dataToDecryptFile
					String dataToDecryptFile = path+"data"+sessionID;
					fp2 =  new File(dataToDecryptFile);
					
					//delete existing file
					if(fp2.exists())
					{
						flag = fp2.delete();				
					}
					
			  		  flag = fp2.createNewFile();
					  fp2 =  new File(dataToDecryptFile);
					  fop = new FileOutputStream(fp2);
					  //System.out.println("file created successfully ::: "+flag);
					  					
					  fop.write(data.getBytes());
					  fop.flush();
					  fop.close();
									
					
					String decryptedFile = path+"decryptedFile"+sessionID;
					
					fp1 = new File(decryptedFile);
					if(fp1.exists())
					{
						flag = fp1.delete();
					}
					flag = fp1.createNewFile();
					fp1 = new File(decryptedFile);
					//Password is null then execute command : openssl rand 100 -out pwdfile else create pwdfile with se
					
					log.info("Start of the Encrypt Pass Phrase method");
					
					String m_cmd = "openssl rsautl -decrypt -inkey " + privateKey + " -in " + dataToDecryptFile + " -out " + decryptedFile;
					//System.out.println(m_cmd);
					Process p = null; 
					try 
					{
						//To execute encrypt command in hex format
						log.info("Start of the ExecuteCryptCommand Method");
						p = Runtime.getRuntime().exec(m_cmd);
						int w = p.waitFor();
						log.info("End of the executeCryptCommand method"); 
						log.info("File write completed");
							 
					} catch (IOException e) {
						log.info("Exception in  Decryption :"+e);
						//e.printStackTrace();
						return 1;
					} catch (InterruptedException e) {
						log.info("Interrupted exception in decryption:"+e);
						//e.printStackTrace();
						return 1;
					}
					finally{
						p.destroy();
					}
					
					
						BufferedReader br = new BufferedReader(new FileReader(decryptedFile));
					    try {
					        StringBuilder sb = new StringBuilder();
					        String line = "";
						
						
						FileInputStream fip = null;
						
						
						line = br.readLine();
						sb.append(line);
						line=null;
					
						while ((line = br.readLine()) != null)
						{
							sb.append("\n");
							sb.append(line);
						}
						
					    log.info("in file content" + sb.toString());
					    log.info("length of line" + sb.length());
					    messageFromServerDecrypted.append(sb.toString());
					    log.info(" messageFromServerin Des " + messageFromServerDecrypted.toString());
					   
					    try
						{
						    /*File fp=new File(path+"randomKey"+sessionID);	
					    	boolean delFlag = fp.delete();
					    	
					    	fp=new File(path+"data"+sessionID);	
					    	delFlag = fp.delete();
					    	
					    	fp=new File(path+"decryptedFile"+sessionID);	
					    	delFlag = fp.delete();*/
					    	
						}
						catch (Exception e) {
							//e.printStackTrace();
							log.info("Exception:",e);
						}
					
					    return 1;
					    
					}
				    catch (Exception e) {
						//e.printStackTrace();
						log.info("Exception:",e);
						return 0;
					}
					    
					}
					catch (Exception e) {
						log.info("Exception:",e);
						return 0;
					}
				
			}
			
			public static byte[] hexStringToByteArray(String s) {
					int len = s.length();
					byte[] data = new byte[len / 8];
					for (int i = 0; i < len; i += 8) {
			    	data[i / 8] = (byte) ((Character.digit(s.charAt(i+6), 16) << 4)
			                             + Character.digit(s.charAt(i+7), 16));
					}
			    return data;
			}
			
			public static String getKey() 
			{
				return key;
			}
			
		

}
