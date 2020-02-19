package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Shariar (Shawn) Emami
 */
@Entity
@Table(name = "item", catalog = "kijijidb", schema = "")
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i join fetch i.category join fetch i.image"),
    @NamedQuery(name = "Item.findById", query = "SELECT i FROM Item i join fetch i.category join fetch i.image WHERE i.id = :id"),
    @NamedQuery(name = "Item.findByPrice", query = "SELECT i FROM Item i join fetch i.category join fetch i.image WHERE i.price = :price"),
    @NamedQuery(name = "Item.findByTitle", query = "SELECT i FROM Item i join fetch i.category join fetch i.image WHERE i.title = :title"),
    @NamedQuery(name = "Item.findByDate", query = "SELECT i FROM Item i join fetch i.category join fetch i.image WHERE i.date = :date"),
    @NamedQuery(name = "Item.findByLocation", query = "SELECT i FROM Item i join fetch i.category join fetch i.image WHERE i.location = :location"),
    @NamedQuery(name = "Item.findByDescription", query = "SELECT i FROM Item i join fetch i.category join fetch i.image WHERE i.description = :description"),
    @NamedQuery(name = "Item.findByUrl", query = "SELECT i FROM Item i join fetch i.category join fetch i.image WHERE i.url = :url"),
    @NamedQuery(name = "Item.findByCategory", query = "SELECT i FROM Item i join fetch i.category join fetch i.image WHERE i.category.id = :categoryid")})
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Size(max = 45)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "url", unique = true)
    private String url;
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Category category;
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Image image;

    public Item() {
    }

    public Item(Integer id) {
        this.id = id;
    }

    public Item(Integer id, BigDecimal price, String title, String description) {
        this.id = id;
        this.price = price;
        this.title = title;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Item[ id=" + id + " ]";
    }
    
}
