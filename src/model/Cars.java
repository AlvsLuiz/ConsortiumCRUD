package model;

import java.util.List;

public class Cars {
	private int id;
	private String make;
	private String model;
	private Integer yearModel;
	private String plate;
	private List<Post> posts;

	public Cars(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getYearModel() {
		return yearModel;
	}
	public void setYearModel(int yearModel) {
		this.yearModel = yearModel;
	}
	public List<Post> getPosts() {
		return posts;
	}
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public void validate() {
		if (model == null || model.isBlank()) 
			throw new IllegalArgumentException("O modelo do carro esta inválido");
		if (yearModel == null)
			throw new IllegalArgumentException("O ano do carro esta inválido");
		if (make == null || make.isBlank())
			throw new IllegalArgumentException("A marca do carro esta inválida");			
	}
	

}
