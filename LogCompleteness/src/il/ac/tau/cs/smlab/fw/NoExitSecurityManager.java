package il.ac.tau.cs.smlab.fw;

import java.security.Permission;

public class NoExitSecurityManager extends SecurityManager 
{
	@Override
	public void checkPermission(Permission perm) 
	{
		// allow anything.
	}

	@Override
	public void checkPermission(Permission perm, Object context) 
	{
		// allow anything.
	}

	@Override
	public void checkExit(int status) 
	{
		super.checkExit(status);
		throw new ExitException(status);
	}


	protected static class ExitException extends SecurityException 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public final int status;
		public ExitException(int status) 
		{
			super(String.valueOf(status));
			this.status = status;
		}
	}

}