package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {

	@DataProvider(name="LoginData")
	public String[][] getData() throws IOException
	{
		String path=".\\testData\\OpenCart_LoginTest.xlsx"; //taking xlfile from testData
		ExcelUtility xlutil=new ExcelUtility(path); //creating an Object for xlUtility
		
		int totalrows=xlutil.getRowCount("Sheet1");
		int totalcols=xlutil.getCellCount("Sheet1",1);
		
		String loginData[][]=new String[totalrows][totalcols];//read data from Excel and store in 2D array
		for(int i=1;i<=totalrows;i++) {// i =1 coz ignore the header in xcel while reading
			for(int j=0;j<totalcols;j++) {
				loginData[i-1][j]=xlutil.getCellData("Sheet1", i, j); //while storing in 2d array pay in i-1 position coz index starts from 0.
			}
		}
		
		return loginData;
	}
	
	//DateProvider 2
	
	//DateProvider 3

	
	//DateProvider 4

	
	//DateProvider 5

	
	//DateProvider 6

}
