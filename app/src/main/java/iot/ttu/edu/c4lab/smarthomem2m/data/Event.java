package iot.ttu.edu.c4lab.smarthomem2m.data;

public class Event {
	private Device device;
	private String resourceid;
	private String value;
	
	public Event(Device device, String resourceid, String value) {
		this.device = device;
		this.resourceid = resourceid;
		this.value = value;
		
		System.out.println(" new  = " + device.getName() + " / " + resourceid + " = " + value);
	}
	
	public Device getDevice() {
		return device;
	}

	public String getResourceid() {
		return resourceid;
	}

	public void setValue(String value) {
		this.value = value;
		// System.out.println("update = " + device.getName() + " / " + resourceid + " = " + value);
	}
	
	public String getValue() {
		return this.value;
	}
}
