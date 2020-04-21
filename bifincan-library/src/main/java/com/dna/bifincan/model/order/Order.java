package com.dna.bifincan.model.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.address.Address;

@Entity
@Table(name = "orders")
@NamedQueries({
	@NamedQuery(name="Order.findPending", query = "select o from Order o where o.placedTime is not null and o.sentTime is null and o.receivedTime is null order by o.id desc"),
	@NamedQuery(name="Order.countPending", query = "select count(o.id) from Order o where o.placedTime is not null and o.sentTime is null and o.receivedTime is null"),
	@NamedQuery(name="Order.countActivePending", query = "select count(o.id) from Order o where o.placedTime is not null and o.sentTime is null and o.receivedTime is null and o.address.user.active = true"),	
	@NamedQuery(name="Order.findInDelivery", query = "select o from Order o where o.placedTime is not null and o.sentTime is not null and o.receivedTime is null order by o.id desc"),
	@NamedQuery(name="Order.countInDelivery", query = "select count(o.id) from Order o where o.placedTime is not null and o.sentTime is not null and o.receivedTime is null"),
	@NamedQuery(name="Order.findReceived", query = "select o from Order o where o.placedTime is not null and o.sentTime is not null and o.receivedTime is not null order by o.id desc"),
	@NamedQuery(name="Order.countReceived", query = "select count(o.id) from Order o where o.placedTime is not null and o.sentTime is not null and o.receivedTime is not null")	
})	
public class Order extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 3699561992290049898L;

	@ManyToOne
	@JoinColumn(name = "address_id", nullable = false)
	private Address address;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "order")
	@OrderBy("id asc")
	private List<OrderDetails> listOfOrderDetails;

	@Column(name = "placed_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date placedTime;

	@Column(name = "sent_time", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date sentTime;

	@Column(name = "received_time", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date receivedTime;

	public Order() {
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<OrderDetails> getListOfOrderDetails() {
		return listOfOrderDetails;
	}

	public void setListOfOrderDetails(List<OrderDetails> listOfOrderDetails) {
		this.listOfOrderDetails = listOfOrderDetails;
	}

	public Date getPlacedTime() {
		return placedTime;
	}

	public void setPlacedTime(Date placedTime) {
		this.placedTime = placedTime;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public Date getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}

	public boolean includes(OrderDetails details) {
		return listOfOrderDetails.contains(details);
	}

	public void addOrderDetails(OrderDetails details) {
		listOfOrderDetails.add(details);
	}

	public void removeOrderDetails(OrderDetails details) {
		listOfOrderDetails.remove(details);
	}

	@Override
	public String toString() {
		return "Order [user=" + address.getUser().getUsername() + ", placedTime=" + placedTime + 
				", sentTime=" + sentTime + ", receivedTime=" + receivedTime + "]";
	}

}
