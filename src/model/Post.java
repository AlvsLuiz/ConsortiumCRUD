package model;

import java.util.Date;

public class Post {
	private int id;
	private String content;
	private Date date;
	private User user;
	private Cars cars;
	
	public Post(int id) {
		this.id = id; 
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Cars getCar() {
		return cars;
	}
	
	public void setCar(Cars car) {
		this.cars = car;
	}
	
	public void validate() {	
		if (content == null || content.isBlank()) {
			throw new IllegalArgumentException("Conteúdo do post não pode ser vazio.");
		}
		
		if (user == null || user.getId() <= 0) {
			throw new IllegalArgumentException("Usuário inválido para o post.");
		}
	}
}
