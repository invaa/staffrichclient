
package net.assino.app1msc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Task complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Task">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Project" type="{http://app1msc.assino.net}Project"/>
 *         &lt;element name="ProjectStage" type="{http://app1msc.assino.net}ProjectStage"/>
 *         &lt;element name="InCharge" type="{http://app1msc.assino.net}Employee"/>
 *         &lt;element name="CurrentStatus" type="{http://app1msc.assino.net}Status"/>
 *         &lt;element name="Deadline" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="DescriptionRu" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Estimate" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="ExternalId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Assignee" type="{http://app1msc.assino.net}Employee"/>
 *         &lt;element name="CurrentAssignee" type="{http://app1msc.assino.net}Employee"/>
 *         &lt;element name="TimeTaken" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Task", propOrder = {
    "name",
    "id",
    "project",
    "projectStage",
    "inCharge",
    "currentStatus",
    "deadline",
    "descriptionRu",
    "estimate",
    "externalId",
    "assignee",
    "currentAssignee",
    "timeTaken"
})
public class Task {

    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Id", required = true)
    protected String id;
    @XmlElement(name = "Project", required = true)
    protected Project project;
    @XmlElement(name = "ProjectStage", required = true, nillable = true)
    protected ProjectStage projectStage;
    @XmlElement(name = "InCharge", required = true)
    protected Employee inCharge;
    @XmlElement(name = "CurrentStatus", required = true, nillable = true)
    protected Status currentStatus;
    @XmlElement(name = "Deadline", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar deadline;
    @XmlElement(name = "DescriptionRu", required = true, nillable = true)
    protected String descriptionRu;
    @XmlElement(name = "Estimate")
    protected float estimate;
    @XmlElement(name = "ExternalId", required = true)
    protected String externalId;
    @XmlElement(name = "Assignee", required = true, nillable = true)
    protected Employee assignee;
    @XmlElement(name = "CurrentAssignee", required = true, nillable = true)
    protected Employee currentAssignee;
    @XmlElement(name = "TimeTaken")
    protected Float timeTaken;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the project property.
     * 
     * @return
     *     possible object is
     *     {@link Project }
     *     
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the value of the project property.
     * 
     * @param value
     *     allowed object is
     *     {@link Project }
     *     
     */
    public void setProject(Project value) {
        this.project = value;
    }

    /**
     * Gets the value of the projectStage property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectStage }
     *     
     */
    public ProjectStage getProjectStage() {
        return projectStage;
    }

    /**
     * Sets the value of the projectStage property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectStage }
     *     
     */
    public void setProjectStage(ProjectStage value) {
        this.projectStage = value;
    }

    /**
     * Gets the value of the inCharge property.
     * 
     * @return
     *     possible object is
     *     {@link Employee }
     *     
     */
    public Employee getInCharge() {
        return inCharge;
    }

    /**
     * Sets the value of the inCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Employee }
     *     
     */
    public void setInCharge(Employee value) {
        this.inCharge = value;
    }

    /**
     * Gets the value of the currentStatus property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Sets the value of the currentStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setCurrentStatus(Status value) {
        this.currentStatus = value;
    }

    /**
     * Gets the value of the deadline property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDeadline() {
        return deadline;
    }

    /**
     * Sets the value of the deadline property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDeadline(XMLGregorianCalendar value) {
        this.deadline = value;
    }

    /**
     * Gets the value of the descriptionRu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionRu() {
        return descriptionRu;
    }

    /**
     * Sets the value of the descriptionRu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionRu(String value) {
        this.descriptionRu = value;
    }

    /**
     * Gets the value of the estimate property.
     * 
     */
    public float getEstimate() {
        return estimate;
    }

    /**
     * Sets the value of the estimate property.
     * 
     */
    public void setEstimate(float value) {
        this.estimate = value;
    }

    /**
     * Gets the value of the externalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Sets the value of the externalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalId(String value) {
        this.externalId = value;
    }

    /**
     * Gets the value of the assignee property.
     * 
     * @return
     *     possible object is
     *     {@link Employee }
     *     
     */
    public Employee getAssignee() {
        return assignee;
    }

    /**
     * Sets the value of the assignee property.
     * 
     * @param value
     *     allowed object is
     *     {@link Employee }
     *     
     */
    public void setAssignee(Employee value) {
        this.assignee = value;
    }

    /**
     * Gets the value of the currentAssignee property.
     * 
     * @return
     *     possible object is
     *     {@link Employee }
     *     
     */
    public Employee getCurrentAssignee() {
        return currentAssignee;
    }

    /**
     * Sets the value of the currentAssignee property.
     * 
     * @param value
     *     allowed object is
     *     {@link Employee }
     *     
     */
    public void setCurrentAssignee(Employee value) {
        this.currentAssignee = value;
    }

    /**
     * Gets the value of the timeTaken property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getTimeTaken() {
        return timeTaken;
    }

    /**
     * Sets the value of the timeTaken property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setTimeTaken(Float value) {
        this.timeTaken = value;
    }

}
