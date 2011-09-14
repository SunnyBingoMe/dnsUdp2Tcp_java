package util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class Statistics{
	private static int	queryNum		= 0;
	private static int	successCount	= 0;
	private static int	failCount		= 0;
	private static long	successTimeSum	= 0;
	private static long	failTimeSum		= 0;

	public static void success(long time){
		queryNum++;
		successCount++;
		successTimeSum += time;
		show();
	}

	public static void fail(long time){
		queryNum++;
		failCount++;
		failTimeSum += time;
		show();
	}

	private static void show(){
		if(queryNum % 100 != 0)
			return;
		try{
			File file = new File("C:\\windows\\temp\\dnsserver.log");
			if(!file.exists())
				file.createNewFile();
			PrintStream out = new PrintStream(file);
			out.println("Total:   " + queryNum);
			out.println("Success: " + successCount);
			out.println("         " + successCount * 100 / queryNum + "%");
			out.println("         " + successTimeAverage() + " ms");
			out.println("Fail:    " + failCount);
			out.println("         " + failCount * 100 / queryNum + "%");
			out.println("         " + failTimeAverage() + " ms");
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	private static long successTimeAverage(){
		if(successCount > 0)
			return successTimeSum / successCount;
		return 0;
	}

	private static long failTimeAverage(){
		if(failCount > 0)
			return failTimeSum / failCount;
		return 0;
	}
}
