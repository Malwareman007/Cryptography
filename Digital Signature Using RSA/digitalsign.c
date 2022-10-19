#include <math.h>
#include <stdio.h>

int main() {
  int a, b, e, c, d1, d2, d, n, bn;
  int arr[100];

  printf("Enter the first prime number: ");
  scanf("%d", &a);

  printf("Enter the second prime number: ");
  scanf("%d", &b);

  printf("Enter the value of e: ");
  scanf("%d", &e);

  printf("Enter the Value of M: ");
  scanf("%d", &c);

  printf("\nUsing RSA Algorithm...");

  n = a * b;
  printf("\n\nn => a*b \nn => %d*%d \nn => %d", a, b, n);

  bn = (a - 1) * (b - 1);
  printf("\n\nQ(n) => (a-1)*(b-1) \nb(n) => (%d-1)*(%d-1) \nb(n) => %d", a, b,
         bn);

  int i = 0;
  d1 = bn;
  d2 = e;
  while (d2 != 0) {
    arr[i++] = d1 / d2;
    d = d1 % d2;
    d1 = d2;
    d2 = d;
  }

  printf("\n\nFinal value of d1 => %d  and  d2 => %d", d1, d2);

  int j = i;
  int t1 = 0;
  int t2 = 1;
  int t;
  if (d1 == 1) {
    for (i = 0; i <= j; i++) {
      t = t1 - (t2 * arr[i]);
      t1 = t2;
      t2 = t;
    }
  }

  printf("\n\nFinal value of t1 => %d  and  t2 => %d", t2, t1);

  if (d1 == 1 & d2 == 0) {
    int d;
    if (t2 < 0) {
      d = bn - (-t2);
    } else {
      d = t2 % bn;
    }

    printf("\n\nValue of d => %dmod%d \n\t\t d => %d", t2, bn, d);

    int f;
    f = (long)(long)pow(c, d) % n;
    printf("\n\nValue of C => %d^%dmod%d \n\t\t C => %d", c, d, n, f);

    int M;
    M = (long)(long)pow(f, e) % n;
    printf("\n\nValue of M => %d^%dmod%d \n\t\t M => %d", f, e, n, M);

    if (M == c) {
      printf("\n\nDigital Signature is Achieved using RSA");
    }
  }

  else {
    printf("Inverse of %d does not exist", e);
  }
  return 0;
}
