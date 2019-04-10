package com.longganisacode.android.vanschedule.schedule;

public class Schedule {
	private Integer id;
	private String time;
	private String vanNumber;
	private String route;
	private String location;
	private String direction;
	private int hour;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getVanNumber() {
		return vanNumber;
	}

	public void setVanNumber(String vanNumber) {
		this.vanNumber = vanNumber;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	@Override
	public String toString() {
		return "Schedule{" +
				"id=" + id +
				", time='" + time + '\'' +
				", vanNumber='" + vanNumber + '\'' +
				", route='" + route + '\'' +
				", location='" + location + '\'' +
				", direction='" + direction + '\'' +
				", hour=" + hour +
				'}';
	}
}