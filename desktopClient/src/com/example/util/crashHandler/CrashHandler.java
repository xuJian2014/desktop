package com.example.util.crashHandler;
import java.lang.Thread.UncaughtExceptionHandler;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Looper;
import android.view.WindowManager;

//UncaughtException������,��������Uncaught�쳣��ʱ��,�ɸ������ӹܳ���,����¼���ʹ��󱨸�.
public class CrashHandler implements UncaughtExceptionHandler
{
	// ϵͳĬ�ϵ�UncaughtException������
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandlerʵ��
	private static CrashHandler INSTANCE;
	// �����Context����
	private Context mContext;
	// ��ֻ֤��һ��CrashHandlerʵ��
	private CrashHandler()
	{
	}
	// ��ȡCrashHandlerʵ�� ,����ģʽ
	public static CrashHandler getInstance()
	{
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}
	//��ʼ��
	public void init(Context context)
	{
		mContext = context;
		// ��ȡϵͳĬ�ϵ�UncaughtException������
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	//��UncaughtException����ʱ��ת�����д�ķ���������
	public void uncaughtException(Thread thread, Throwable ex)
	{
		if (!handleException(ex) && mDefaultHandler != null)
		{
			// ����Զ����û�д�������ϵͳĬ�ϵ��쳣������������
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
            //�˳�����  
            android.os.Process.killProcess(android.os.Process.myPid());  
            System.exit(1);  
        }  
	}
	/**
	 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
	 * 
	 * @param ex
	 *            �쳣��Ϣ
	 * @return true ��������˸��쳣��Ϣ;���򷵻�false.
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
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_info).setTitle("Ӧ�ó������")
				.setMessage("�ܱ�Ǹ��Ӧ�ó�����ִ��󣬼����˳�"+"\n"+"������Ϣ"+"\n"+crashReport)
				.setPositiveButton("ȷ��",new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,int which)
					{
						dialog.dismiss();
						// �˳�
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(1);
					}
				});
				/*.setNegativeButton("ȡ��",new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,int which)
					{
						dialog.dismiss();
						// �˳�
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(1);
					}
				});*/

		AlertDialog dialog = builder.create();
		// ��Ҫ�Ĵ��ھ����ʽ��û�����ᱨ���
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}
	/**
	 * ��ȡAPP�����쳣����
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
	 * ��ȡApp��װ����Ϣ
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