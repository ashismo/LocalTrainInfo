package com.app.ashish.localtraininfo.bean;

public class TrainScheduleBean {
	private String slNo;
	private String stationName;
	private String schDept;
	private String schArr;
	private String date;
	private String actDept;
	private String actArr;
	private String km;
	private boolean isTrainReachedDest;
	private String currentStatus;
	private String trainName;
	
	public String getSlNo() {
		return slNo;
	}
	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getSchDept() {
		return schDept;
	}
	public void setSchDept(String schDept) {
		this.schDept = schDept;
	}
	public String getSchArr() {
		return schArr;
	}
	public void setSchArr(String schArr) {
		this.schArr = schArr;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getActDept() {
		return actDept;
	}
	public void setActDept(String actDept) {
		this.actDept = actDept;
	}
	public String getActArr() {
		return actArr;
	}
	public void setActArr(String actArr) {
		this.actArr = actArr;
	}
	public String getKm() {
		return km;
	}
	public void setKm(String km) {
		this.km = km;
	}
	
	public boolean isTrainReachedDest() {
		return isTrainReachedDest;
	}
	public void setTrainReachedDest(boolean isTrainReachedDest) {
		this.isTrainReachedDest = isTrainReachedDest;
	}
	
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	
	public String getTrainName() {
		return trainName;
	}
	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}
	@Override
	public String toString() {
		return "TrainScheduleBean [slNo=" + slNo + ", stationName="
				+ stationName + ", schDept=" + schDept + ", schArr=" + schArr
				+ ", date=" + date + ", actDept=" + actDept + ", actArr="
				+ actArr + ", km=" + km + ", isTrainReachedDest="
				+ isTrainReachedDest + ", currentStatus=" + currentStatus
				+ ", trainName=" + trainName + "]";
	}
	
	
}
