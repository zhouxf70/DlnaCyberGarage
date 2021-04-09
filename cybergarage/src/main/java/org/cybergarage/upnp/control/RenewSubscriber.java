/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: RenewSubscriber.java
*
*	Revision:
*
*	07/07/04
*		- first revision.
*	
******************************************************************/

package org.cybergarage.upnp.control;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.ThreadCore;

public class RenewSubscriber extends ThreadCore
{
	public final static long INTERVAL = 120;
	
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public RenewSubscriber(ControlPoint ctrlp)
	{
		setControlPoint(ctrlp);
	}
	
	////////////////////////////////////////////////
	//	Member
	////////////////////////////////////////////////

	private ControlPoint ctrlPoint;

	public void setControlPoint(ControlPoint ctrlp)
	{
		ctrlPoint = ctrlp;
	}
	
	public ControlPoint getControlPoint()
	{
		return ctrlPoint;
	}

	////////////////////////////////////////////////
	//	Thread
	////////////////////////////////////////////////
	
	public void run() 
	{
		ControlPoint ctrlp = getControlPoint();
		long renewInterval = INTERVAL * 1000;
		while (isRunnable()) {
			try {
				Thread.sleep(renewInterval);
			} catch (InterruptedException e) {}
			ctrlp.renewSubscriberService();
		}
	}
}
