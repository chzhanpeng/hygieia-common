package com.capitalone.dashboard.model.quality;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="checkstyle")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckstyleReport implements QualityVisitee {

    @XmlElement(name="file")
    private List<CheckstyleFile> files;

    @Override
    public void accept(QualityVisitor visitor) {
        visitor.visit(this);
    }

    public List<CheckstyleFile> getFiles() {
        return files;
    }

    public void setFiles(List<CheckstyleFile> files) {
        this.files = files;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CheckstyleFile {

        @XmlElement(name="error")
        private List<CheckstyleError> errors;

        public List<CheckstyleError> getErrors() {
            return errors;
        }

        public void setErrors(List<CheckstyleError> errors) {
            this.errors = errors;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CheckstyleError {

        @XmlAttribute(name="severity")
        private CheckstyleSeverity severity;


        public CheckstyleSeverity getSeverity() {
            return severity;
        }

        public void setSeverity(CheckstyleSeverity severity) {
            this.severity = severity;
        }
    }

    public enum CheckstyleSeverity {
        error,warning,info,ignore
    }
}
