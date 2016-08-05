package edu.pdx.cs410J.ew4.client;

import com.google.gwt.i18n.shared.DateTimeFormat;
import edu.pdx.cs410J.AbstractAppointment;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * The type Appointment.
 */
public class Appointment extends AbstractAppointment implements Comparable<Appointment> {
  private String description = "< empty description >";
  private Date beginTime;
  private Date endTime;


  /**
   * Instantiates a new Appointment.
   */
  public Appointment() {

  }

  /**
   * Instantiates a new Appointment.
   *
   * @param description the description
   * @param beginTime   the begin time
   * @param endTime     the end time
   */
  public Appointment(String description, Date beginTime, Date endTime) {
    this.description = description;
    this.beginTime = beginTime;
    this.endTime = endTime;
  }

  private String formatDate(Date date) {
    String pattern = "MM/dd/yyyy hh:mm a";
    return DateTimeFormat.getFormat(pattern).format(date);
  }

  /**
   * Returns a brief textual summary of this appointment.
   */
  @Override
  public String toString() {
    return super.toString();
  }

  /**
   * Returns a hash code value for the object. This method is
   * supported for the benefit of hash tables such as those provided by
   * {@link HashMap}.
   * <p>
   * The general contract of {@code hashCode} is:
   * <ul>
   * <li>Whenever it is invoked on the same object more than once during
   * an execution of a Java application, the {@code hashCode} method
   * must consistently return the same integer, provided no information
   * used in {@code equals} comparisons on the object is modified.
   * This integer need not remain consistent from one execution of an
   * application to another execution of the same application.
   * <li>If two objects are equal according to the {@code equals(Object)}
   * method, then calling the {@code hashCode} method on each of
   * the two objects must produce the same integer result.
   * <li>It is <em>not</em> required that if two objects are unequal
   * according to the {@link Object#equals(Object)}
   * method, then calling the {@code hashCode} method on each of the
   * two objects must produce distinct integer results.  However, the
   * programmer should be aware that producing distinct integer results
   * for unequal objects may improve the performance of hash tables.
   * </ul>
   * <p>
   * As much as is reasonably practical, the hashCode method defined by
   * class {@code Object} does return distinct integers for distinct
   * objects. (This is typically implemented by converting the internal
   * address of the object into an integer, but this implementation
   * technique is not required by the
   * Java&trade; programming language.)
   *
   * @return a hash code value for this object.
   * @see Object#equals(Object)
   * @see System#identityHashCode
   */
  @Override
  public int hashCode() {
    return Objects.hash(description, this.getBeginTimeString(), this.getEndTimeString(), beginTime, endTime);
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   * <p>
   * The {@code equals} method implements an equivalence relation
   * on non-null object references:
   * <ul>
   * <li>It is <i>reflexive</i>: for any non-null reference value
   * {@code x}, {@code x.equals(x)} should return
   * {@code true}.
   * <li>It is <i>symmetric</i>: for any non-null reference values
   * {@code x} and {@code y}, {@code x.equals(y)}
   * should return {@code true} if and only if
   * {@code y.equals(x)} returns {@code true}.
   * <li>It is <i>transitive</i>: for any non-null reference values
   * {@code x}, {@code y}, and {@code z}, if
   * {@code x.equals(y)} returns {@code true} and
   * {@code y.equals(z)} returns {@code true}, then
   * {@code x.equals(z)} should return {@code true}.
   * <li>It is <i>consistent</i>: for any non-null reference values
   * {@code x} and {@code y}, multiple invocations of
   * {@code x.equals(y)} consistently return {@code true}
   * or consistently return {@code false}, provided no
   * information used in {@code equals} comparisons on the
   * objects is modified.
   * <li>For any non-null reference value {@code x},
   * {@code x.equals(null)} should return {@code false}.
   * </ul>
   * <p>
   * The {@code equals} method for class {@code Object} implements
   * the most discriminating possible equivalence relation on objects;
   * that is, for any non-null reference values {@code x} and
   * {@code y}, this method returns {@code true} if and only
   * if {@code x} and {@code y} refer to the same object
   * ({@code x == y} has the value {@code true}).
   * <p>
   * Note that it is generally necessary to override the {@code hashCode}
   * method whenever this method is overridden, so as to maintain the
   * general contract for the {@code hashCode} method, which states
   * that equal objects must have equal hash codes.
   *
   * @param o the reference object with which to compare.
   * @return {@code true} if this object is the same as the obj
   * argument; {@code false} otherwise.
   * @see #hashCode()
   * @see HashMap
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Appointment that = (Appointment) o;
    return Objects.equals(description, that.description) &&
            Objects.equals(this.getBeginTimeString(), that.getBeginTimeString()) &&
            Objects.equals(this.getEndTimeString(), that.getEndTimeString()) &&
            Objects.equals(beginTime, that.beginTime) &&
            Objects.equals(endTime, that.endTime);
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.
   * <p>
   * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
   * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
   * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
   * <tt>y.compareTo(x)</tt> throws an exception.)
   * <p>
   * <p>The implementor must also ensure that the relation is transitive:
   * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
   * <tt>x.compareTo(z)&gt;0</tt>.
   * <p>
   * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
   * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
   * all <tt>z</tt>.
   * <p>
   * <p>It is strongly recommended, but <i>not</i> strictly required that
   * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
   * class that implements the <tt>Comparable</tt> interface and violates
   * this condition should clearly indicate this fact.  The recommended
   * language is "Note: this class has a natural ordering that is
   * inconsistent with equals."
   * <p>
   * <p>In the foregoing description, the notation
   * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
   * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
   * <tt>0</tt>, or <tt>1</tt> according to whether the value of
   * <i>expression</i> is negative, zero or positive.
   *
   * @param that the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException   if the specified object's type prevents it
   *                              from being compared to this object.
   */
  @Override
  public int compareTo(Appointment that) {
    if (this == that) return 0;
    if (that == null) {
      throw new NullPointerException("Cannot compare null appointment");
    }
    if (this.beginTime.compareTo(that.beginTime) == 0) {
      if (this.endTime.compareTo(that.endTime) == 0) {
        return this.description.compareTo(that.description);
      } else {
        return this.endTime.compareTo(that.endTime);
      }
    } else {
      return this.beginTime.compareTo(that.beginTime);
    }
  }

  /**
   * Returns a String describing the beginning date and time of this
   * appointment.
   */
  @Override
  public String getBeginTimeString() {
    return /*"START " + */formatDate(this.beginTime);
  }

  /**
   * Returns a String describing the ending date and time of this
   * appointment.
   */
  @Override
  public String getEndTimeString() {
    return /*"END " + */formatDate(this.endTime);
  }

  /**
   * Returns the {@link Date} that this appointment ends.
   */
  @Override
  public Date getEndTime() {
    return this.endTime;
  }

  /**
   * Returns a description of this appointment (for instance,
   * <code>"Have coffee with Marsha"</code>).
   */
  @Override
  public String getDescription() {
    return this.description;
  }

  /**
   * Returns the {@link Date} that this appointment begins.
   */
  @Override
  public Date getBeginTime() {
    return this.beginTime;
  }


  /**
   * Gets duration.
   *
   * @return the duration as a <code>String</code>
   */
  public String getDuration() {

    long diff = abs(this.beginTime.getTime() - this.endTime.getTime());
    long diffMinutes = diff / (60 * 1000) % 60;
    long diffHours = diff / (60 * 60 * 1000);

    StringBuilder sb = new StringBuilder();
    if (diffHours != 0) {
      sb.append(diffHours).append(" hours ");
    }
    sb.append(diffMinutes).append(" minutes");

    return sb.toString();
  }
}
