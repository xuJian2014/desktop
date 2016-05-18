package com.example.util.crashHandler;
import java.lang.Thread.UncaughtExceptionHandler;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Looper;
import android.view.WindowManager;

//UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
public class CrashHandler implements UncaughtExceptionHandler
{
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler INSTANCE;
	// 程序的Context对象
	private Context mContext;
	// 保证只有一个CrashHandler实例
	private CrashHandler()
	{
	}
	// 获取CrashHandler实例 ,单例模式
	public static CrashHandler getInstance()
	{
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}
	//初始化
	public void init(Context context)
	{
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	//当UncaughtException发生时会转入该重写的方法来处理
	public void uncaughtException(Thread thread, Throwable ex)
	{
		if (!handleException(ex) && mDefaultHandler != null)
		{
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		}
		else 
		{  
            try 
            {  
                Thread.sleep(3000);  
            } 
            catch (InterruptedException e)
            {  
            	e.printStackTrace(); 
            }  
            //退出程序  
            android.os.Process.killProcess(android.os.Process.myPid());  
            System.exit(1);  
        }  
	}
	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 *            异常信息
	 * @return true 如果处理了该异常信息;否则返回false.
	 */
	public boolean handleException(Throwable ex)
	{
		if (ex == null || mContext == null)
			return false;
		final String crashReport = getCrashReport(mContext, ex);
		new Thread()
		{
			public void run()
			{
				Looper.prepare();
				//File file = save2File(crashReport);
				//sendAppCrashReport(mContext, crashReport, file);
				crashReport(mContext,crashReport);
				Looper.loop();
			}
		}.start();
		
		
		return true;
	}

	private void crashReport(final Context context,final String crashReport)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_info).setTitle("应用程序错误")
				.setMessage("很抱歉，应用程序出现错误，即将退出"+"\n"+"错误信息"+"\n"+crashReport)
				.setPositiveButton("确定",new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,int which)
					{
						dialog.dismiss();
						// 退出
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(1);
					}
				});
				/*.setNegativeButton("取消",new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,int which)
					{
						dialog.dismiss();
						// 退出
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(1);
					}
				});*/

		AlertDialog dialog = builder.create();
		// 需要的窗口句柄方式，没有这句会报错的
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}
	/**
	 * 获取APP崩溃异常报告
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex)
	{
		PackageInfo pinfo = getPackageInfo(context);
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: " + pinfo.versionName + "("+ pinfo.versionCode + ")\n");
		exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE+"(" + android.os.Build.MODEL + ")\n");
		exceptionStr.append("Exception: " + ex.getMessage() + "\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++)
		{
			exceptionStr.append(elements[i].toString() + "\n");
		}
		return exceptionStr.toString();
	}
	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	private PackageInfo getPackageInfo(Context context)
	{
		PackageInfo info = null;
		try
		{
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} 
		catch (NameNotFoundException e)
		{
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}
}