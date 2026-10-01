import java.util.Arrays;

public class findElement
{
public static void main(String []args)
{
int [] arrayname = new int [] {1, 2, 3, 4, 2, 7, 8, 3};
int size;
size = arrayname.length;
for(int i=0;i<size;i++) //Use to hold an element
for(int j=i+1;j<size;j++) //Use to check for rest of the elements
{
if(arrayname[i]>arrayname[j]) //Compare and swap
{
int temp=arrayname[i];
arrayname[i]=arrayname[j];
arrayname[j]=temp;
}}
System.out.println("testing12344 Largest element is"+arrayname[size-1]);
//Display Largest
System.out.println("2nd Largest element is"+arrayname[size-2]);
}
}
