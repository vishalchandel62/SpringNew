package com.credentek.msme.loginmodel;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.credentek.msme.utils.Utils;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import net.sf.json.JSONObject;

public class FirstPdf {
	
	 private static Font labelFont1 = new Font(Font.FontFamily.UNDEFINED, 12, Font.BOLD,BaseColor.BLACK);
	    
	    private static Font normalBlack20 = new Font(Font.FontFamily.UNDEFINED, 20, Font.NORMAL, BaseColor.BLACK);
	    private static Font boldBlack15 = new Font(Font.FontFamily.UNDEFINED, 15, Font.BOLD,BaseColor.BLACK);
	    private static Font normalBlack13= new Font(Font.FontFamily.UNDEFINED, 13, Font.NORMAL,BaseColor.BLACK);
	    private static Font normalBlack10= new Font(Font.FontFamily.UNDEFINED, 10, Font.NORMAL,BaseColor.BLACK);
	    
	    private static Font labelFont4= new Font(Font.FontFamily.UNDEFINED, 8, Font.NORMAL,BaseColor.BLACK);
	    
	    private static final Logger log = LogManager.getLogger(FirstPdf.class);
	    
	    public FirstPdf(String fileName,String tempFileName,String imgLogoPath,String backImgPath,String strHeader,String extraRemark,JSONObject jsonData){
	    	try {
	    		log.info("jsonData="+jsonData);
	    		//create document for PDF 
	    		Document document = new Document();
	    		PdfWriter pdfWriter= PdfWriter.getInstance(document, new FileOutputStream(fileName));
	    		document.open();
	    		
	    		addMetaData(document);//********Add PDF Meta Data  *******
	    		
	    		addLogo(document,imgLogoPath,jsonData);//*******Add YES bank Logo on top of PDF
	    		
	    		addTitlePage(document,strHeader,jsonData);//*******Add Title on top of PDF
	    		
	    		addEmptyLine(document, 1); //******Add Empty Line
	    		
	    		addHeaderFooterPage(pdfWriter,document);
	    		
	    		document.close();

	    		addBackgroundImage(fileName,tempFileName,backImgPath); //******Add Background Image on all pages

	    		
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		System.out.println("EXception :::"+ e);
	    	}
	    }
	    

		private void addLogo(Document document, String imgLogoPath, JSONObject jsonData) {
	    	try {
				Image imgLogo = Image.getInstance(imgLogoPath);
				imgLogo.scaleToFit(90f, 180f);
				imgLogo.setAlignment(Image.ALIGN_RIGHT);
				document.add(imgLogo);
				
				Paragraph dateHeader = new Paragraph(jsonData.getString("responseDate"), normalBlack10);
				dateHeader.setAlignment(Element.ALIGN_RIGHT);
				document.add(dateHeader);
				
			} catch (BadElementException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			
		}

		public static void main(String[] args) {
	        try {
	        	String FILE = "/home/credentek/oracle/Desktop/BULK-FILE/PDF/FirstPdf525252.pdf";
	        	String FILE1 = "/home/credentek/oracle/Desktop/BULK-FILE/PDF/FirstPdf525252_tyemp.pdf";

	        	String imgLogoPath =  "/home/credentek/oracle/Desktop/BULK-FILE"+"/PDF/Yes_Bank_Logo.png";
	        	String backImgPath =  "/home/credentek/oracle/Desktop/BULK-FILE"+"/PDF/tick2.png";
	        	String extraRemark =" My extra note and remark will apprear here remark. PLease using parameter";
	        
	        	String jsonString="{\"DataArray\":{\"STATUS\":\"SUCCESS\",\"CASEID\":\"CE070122517091\",\"REMARKS\":\"eeeee ewf egf  regerg retg reg regre gtrdgregfrgdr gseghtr erhgerhy 22541 ergerg ter52 3222erg ergt2322 ewr trwetr3688et ertre tre8559hg eryery85nb ertertw  ewr89998h awetaw3t 5wawerwr werwer8r 85e5 85wq 69q7 wq5 wr we4t4we6566445w e4twet wer656654wetwet aweetwt a wt 664er etewt aw6564y twetw654 wtwt6544 wtqw 65474 w yw3we6+ w wt4654wy wttye w\"},\"responseDate\":\"07-January, 2022  \",\"strBodyMsg\":\"Below are your Service Request Transaction Details performed on 07-January, 2022\",\"CustomerID\":\"666555\",\"custName\":\"SRJ\"}";
	        	JSONObject jsonData = JSONObject.fromObject(jsonString);
	        	
	        	FirstPdf pdf = new FirstPdf(FILE,FILE1,imgLogoPath,backImgPath,"Service Request Transaction Details",extraRemark,jsonData);
	        	System.out.println("yest");

	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    // Add metadata
	    public static void addMetaData(Document document) {
	        document.addTitle("PDF");
	        document.addSubject("YES Bank");
	        document.addKeywords("Report");
	        document.addAuthor("YES Bank");
	        document.addCreator("YES Bank");
	    }

	    public static void addTitlePage(Document document,String strHeader,JSONObject jsonData)   throws DocumentException {

			try {
				Paragraph paraGrp = new Paragraph(strHeader,normalBlack20);
				paraGrp.setAlignment(Element.ALIGN_LEFT);
				paraGrp.setSpacingBefore(-30);
				document.add(new Paragraph(paraGrp));
				
				addEmptyLine(document, 1); //******Add Empty Line
				
				List list = new List(false, false, 0);
				list.setListSymbol("");
				list.add(new ListItem("Dear "+jsonData.getString("custName")+",",boldBlack15));
				list.add(new ListItem("Customer ID :"+jsonData.getString("CustomerID"),normalBlack13));
				document.add(list);
				
				addEmptyLine(document, 2); //******Add Empty Line

				Paragraph paraGrpBody = new Paragraph(jsonData.getString("strBodyMsg"),normalBlack13);
				paraGrpBody.setAlignment(Element.ALIGN_LEFT);

				document.add(new Paragraph(paraGrpBody));
				
				addEmptyLine(document, 1); //******Add Empty Line
				
				int colCount=3;
	    		
				PdfPTable table = new PdfPTable(colCount); // number of columns
				
				int[] colWidthArray= new int[] {25,0,75};
				
				table.setWidths(colWidthArray);// Specify Width of each cell
				
				table.setWidthPercentage(100);
		        table.setSpacingBefore(00f);
		        table.setSpacingAfter(00f);  
				
		        if(jsonData.has("DataArray")) {
		        	
		        	JSONObject dataArrayObj= jsonData.getJSONObject("DataArray");
		        	
		        	String caseId= dataArrayObj.getString("CASEID"); 
		    		String remark= dataArrayObj.getString("REMARKS"); 
		    		String statusStr=  dataArrayObj.getString("STATUS"); 	    		
		    		String turnaAroundTime=  dataArrayObj.getString("turnAroundTime"); //TAT_CR

		        	
		    		PdfPCell cell = new PdfPCell(getCustumCell("Case Id","name"));
		    		cell.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell);
		    		
		    		cell = new PdfPCell(getCustumCell("","value"));
		    		cell.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell);
		    		
		    		cell = new PdfPCell(getCustumCell(caseId,"value"));
		    		cell.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell);
		    		
		    		
		    		PdfPCell cell2 = new PdfPCell(getCustumCell("Status","name"));
		    		cell2.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell2);
		    		
		    		cell2 = new PdfPCell(getCustumCell("","value"));
		    		cell2.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell2);
		    		
		    		cell2 = new PdfPCell(getCustumCell(statusStr,"value"));
		    		cell2.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell2);

		    		//-----------TAT_CR--------------------------
		    		PdfPCell cell4 = new PdfPCell(getCustumCell("Turnaround Time","name"));   
		    		cell4.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell4);
		    		
		    		cell4 = new PdfPCell(getCustumCell("","value"));
		    		cell4.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell4);
		    		
		    		cell4 = new PdfPCell(getCustumCell(turnaAroundTime,"value"));
		    		cell4.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell4);
				//-----------End--------------------------
		    		
		    		PdfPCell cell3 = new PdfPCell(getCustumCell("Remark","name"));
		    		cell3.setBorder(Rectangle.NO_BORDER);
		    		cell3.setPaddingTop(-20.0f);
		    		cell3.setPaddingBottom(20.0f);
		    		cell3.setFixedHeight(70f);
		    		table.addCell(cell3);
		    		
		    		cell3 = new PdfPCell(getCustumCell("","value"));
		    		cell3.setPaddingTop(-20.0f);
		    		cell3.setPaddingBottom(20.0f);
		    		cell3.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell3);
		    		
		    		cell3 = new PdfPCell(getCustumCell(remark,"value"));
		    		cell3.setPaddingTop(-20.0f);
		    		cell3.setPaddingBottom(20.0f);
		    		cell3.setBorder(Rectangle.NO_BORDER);
		    		table.addCell(cell3);
		    		
		    		
		    		
		    		
		        }
		        
				document.add(table);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.info("sddd"+e);
			}
	        
	    }

	    public static PdfPCell getCustumCell(String strData, String cellType)  {
	    	
	    	Font f  = new Font();
	    	
	    	if("name".equals(cellType)) 
	    	{
	    		f=labelFont1;
	    	}
	    	else if("value".equals(cellType)) {
	    		f=normalBlack10;
	    	}
	    	else {
	    		f=normalBlack10;
	    	}

	    	PdfPCell cell = new PdfPCell(new Phrase(strData, f));
	    	//cell.setFixedHeight(35f);
	    	cell.setFixedHeight(25f);
	    	cell.setNoWrap(false);
			cell.setBorder(Rectangle.RIGHT);
			cell.setBorderColor(new BaseColor(0,0,0));
			cell.setBackgroundColor(new BaseColor(255, 255, 255));;
			cell.setPaddingLeft(10f);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	    	return cell;
	    }
	    
	    private static void addEmptyLine(Document document, int number) {
	    	
	        try {
				for (int i = 0; i < number; i++) {
					Paragraph  para= new Paragraph(" ");
					para.setAlignment(Rectangle.ALIGN_CENTER);
					document.add(para);
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
	    }
	    
	    private void addBackgroundImage(String fileName, String tempFileName, String backImgPath) {
	    	try {
				PdfReader reader = new PdfReader(fileName);
				int n = reader.getNumberOfPages();
				PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(tempFileName));
				stamper.setRotateContents(false);

				Image img = Image.getInstance(backImgPath);
				img.scaleAbsolute(PageSize.A4.rotate());
				float w = img.getScaledWidth();
				float h = img.getScaledHeight();
				// transparency
				PdfGState gs1 = new PdfGState();
				gs1.setFillOpacity(0.20f);
				// properties
				PdfContentByte over;
				Rectangle pagesize;
				float x, y;
				// loop over every page
				for (int i = 1; i <= n; i++) {
					pagesize = reader.getPageSize(i);
					x = (pagesize.getLeft() + pagesize.getRight()) / 2;
					y = (pagesize.getTop() + pagesize.getBottom()) / 2;
					over = stamper.getOverContent(i);
					over.saveState();
					over.setGState(gs1);
					over.addImage(img, w/2, 0, 0, h, x-(x/2)+50 , y - (h / 2));
					over.restoreState();
				}
				stamper.close();
				reader.close();
				System.out.println("yes pdf done");
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    public void addHeaderFooterPage(PdfWriter writer,Document document) {
	    	PdfContentByte cb = writer.getDirectContent();
	        Phrase header = new Phrase("", labelFont4);
	        Phrase footer = new Phrase("Created On : "+Utils.formatDate(new Date(), "dd/MM/yyyy HH:mm:ss"), labelFont4);
	        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
	                header,
	                (document.right() - document.left()) / 2 + document.leftMargin(),
	                document.top() + 10, 0);
	        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
	                footer,
	                (document.right() - document.left()) / 2 + document.leftMargin(),
	                document.bottom() - 10, 0);
	    }
	    
	    
	

}
