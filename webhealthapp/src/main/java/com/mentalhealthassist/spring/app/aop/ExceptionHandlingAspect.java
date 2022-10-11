package com.mentalhealthassist.spring.app.aop;

import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;

@Aspect
@Component
public class ExceptionHandlingAspect {
	
	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	
	@Around("execution(* com.mentalhealthassist.spring.app.service.TreatmentServiceImpl.saveTreatment(*))")
	public Object aroundSave(
				ProceedingJoinPoint theProceedingJoinPoint) throws Throwable{
		
		System.out.println("WE IN");
		
		//create object to catch result
		Object result = null;
		
		myLogger.info(">>>>>> Inside aspect");
		
		try {
			
			//execute the method
			result = theProceedingJoinPoint.proceed();
			
		}catch(TransactionSystemException e) {
			//log the exception 
			myLogger.warning(e.getMessage());
			
			result = false;
		}
		
		
		
		return result;
	}
	

}
