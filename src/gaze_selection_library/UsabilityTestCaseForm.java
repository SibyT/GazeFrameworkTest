package gaze_selection_library;

import java.util.Date;

public class UsabilityTestCaseForm {
private String userName= null;
private  String age ;
private  String gender= null;
private  String gazeDataProviderType= null;
private  String targetSelectionType= null;
private  String triggerType= null;
private  Date testDate =null;
private int partipantId= 0;



	public int getPartipantId() {
	return partipantId;
}



public void setPartipantId(int partipantId) {
	this.partipantId = partipantId;
}



	public UsabilityTestCaseForm() {
		// TODO Auto-generated constructor stub
		
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	



	public String getGender() {
		return gender;
	}



	public void setGender(String gender) {
		this.gender = gender;
	}



	public String getGazeDataProviderType() {
		return gazeDataProviderType;
	}



	public void setGazeDataProviderType(String gazeDataProviderType) {
		this.gazeDataProviderType = gazeDataProviderType;
	}



	public String getTargetSelectionType() {
		return targetSelectionType;
	}



	public void setTargetSelectionType(String targetSelectionType) {
		this.targetSelectionType = targetSelectionType;
	}



	public String getTriggerType() {
		return triggerType;
	}



	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}



	public Date getTestDate() {
		return testDate;
	}



	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}



	public String getAge() {
		return age;
	}



	public void setAge(String age) {
		this.age = age;
	}

}
