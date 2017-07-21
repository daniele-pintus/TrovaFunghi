package trova.funghi.handler;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BaseUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
	
//	private final Context myContext;
//	private final Class<?> myActivityClass;

	public BaseUncaughtExceptionHandler() {

//		myContext = context;
//		myActivityClass = c;
	}

	public void uncaughtException(Thread thread, Throwable exception) {

		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		System.err.println(stackTrace);// You can use LogCat too
		
//		Intent intent = new Intent(myContext, myActivityClass);
//		String s = stackTrace.toString();
//		//you can use this String to know what caused the exception and in which Activity
//		intent.putExtra("uncaughtException", "Exception is: " + stackTrace.toString());
//		intent.putExtra("stacktrace", s);
//		myContext.startActivity(intent);
		//for restarting the Activity
		
		System.err.println("Force application exit");
		
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
