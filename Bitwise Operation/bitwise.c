#include <stdio.h>
#include <stdlib.h>

int length=0;

void display(int *x,int size)
{
    for(int i=0; i<size; i++)
        printf("%d",x[i]);
    printf("\n");
}

int * toBinary(int x)
{
    printf("binary of %d: ",x);
    int q=0;
    int y=x;
    while(y>0) {
        y=y/2;
        q++;
    }
    int size=q;
    int *binary=(int *)malloc(sizeof(int)*size);
    while(x>0) {
        binary[q-1]=x%2;
        x=x/2;
        q--;
    }
    display(binary,size);
    return binary;
}

int * equate(int *a,int al,int *b,int bl)
{
    int count=0;
    int size;
    size=(al>bl)?al:bl;
    length=size;
    int *newb=(int *)malloc(sizeof(int)*size);
    int minlength;
    minlength=(al>bl)?bl:al;
    for(int i=0; i<size-minlength; i++) {
        newb[i]=0;
        count++;
    }
    int *ar;
    if(al==size)ar=b;
    else ar=a;
    for(int i=count; i<size; i++) {
        newb[i]=ar[i-count];
    }
    return newb;
}

void OR(int * a,int *b,int al)
{
    printf("\nOR Operation: ");
    int * orr=(int *)malloc(sizeof(int)*al);
    for(int i=0; i<al; i++) {
        if(a[i]==0&&b[i]==0)orr[i]=0;
        else orr[i]=1;
    }
    display(orr,al);
}

void AND(int * a,int *b,int al)
{
    printf("AND Operation: ");
    int * andd=(int *)malloc(sizeof(int)*al);
    for(int i=0; i<al; i++) {
        andd[i]=a[i]*b[i];
    }
    display(andd,al);
}

void XOR(int * a,int *b,int al)
{
    printf("XOR Operation: ");
    int * xorr=(int *)malloc(sizeof(int)*al);
    for(int i=0; i<al; i++) {
        if(a[i]==b[i])xorr[i]=0;
        else xorr[i]=1;
    }
    display(xorr,al);
}


int main()
{
    int a,b;
    printf("Enter number1: ");
    scanf("%d",&a);
    int *num1=toBinary(a);
    int num1size=0;
    int x=a;
    while(a>0) {
        a=a/2;
        num1size++;
    }
    printf("\nEnter number2: ");
    scanf("%d",&b);
    int y=b;
    int *num2=toBinary(b);
    int num2size=0;
    while(b>0) {
        b=b/2;
        num2size++;
    }
    if(num1size>num2size) {
        num2=equate(num1,num1size,num2,num2size);
        printf("new %d = ",y);
        display(num2,length);
    } else if(num1size<num2size) {
        num1=equate(num1,num1size,num2,num2size);
        printf("new %d = ",x);
        display(num1,length);
    } else {
        length=num1size;
    }
    OR(num1,num2,length);
    AND(num1,num2,length);
    XOR(num1,num2,length);
    return 0;
}
