#include <stdio.h>
#include <inttypes.h>
#include <byteswap.h>
#include <string.h>

#define WORD uint64_t
#define PF PRIx64
#define BYTE uint8_t

const int _i = 1;
#define is_little_endian() ((*(char*)&_i) != 0) // char = 8 bits


#define ROTR(_x, _n) (((_x) >> (_n)) | ((_x) << ((sizeof(_x) * 8) - (_n))))

#define SHR(_x, _n) ((_x) >> (_n))

#define CH(_x, _y, _z) (((_x) & (_y)) ^ (~(_x) & (_z)))


#define MAJ(_x, _y, _z) (((_x) & (_y)) ^ ((_x) & (_z)) ^ ((_y) & (_z)))
#define SIG0(_x) (ROTR(_x, 28) ^ ROTR(_x, 34) ^ ROTR(_x, 39))
#define SIG1(_x) (ROTR(_x, 14) ^ ROTR(_x, 18) ^ ROTR(_x, 41))
#define Sig0(_x) (ROTR(_x, 1) ^ ROTR(_x, 8) ^ SHR(_x, 7))
#define Sig1(_x) (ROTR(_x, 19) ^ ROTR(_x, 61) ^ SHR(_x, 6))

union Block {
    BYTE bytes[128];
    WORD words[16];
    uint64_t sixF[16];
};

enum Status {
    READ, PAD, END
};


const WORD K[] = {
        0x428a2f98d728ae22, 0x7137449123ef65cd, 0xb5c0fbcfec4d3b2f, 0xe9b5dba58189dbbc,
        0x3956c25bf348b538, 0x59f111f1b605d019, 0x923f82a4af194f9b, 0xab1c5ed5da6d8118,
        0xd807aa98a3030242, 0x12835b0145706fbe, 0x243185be4ee4b28c, 0x550c7dc3d5ffb4e2,
        0x72be5d74f27b896f, 0x80deb1fe3b1696b1, 0x9bdc06a725c71235, 0xc19bf174cf692694,
        0xe49b69c19ef14ad2, 0xefbe4786384f25e3, 0x0fc19dc68b8cd5b5, 0x240ca1cc77ac9c65,
        0x2de92c6f592b0275, 0x4a7484aa6ea6e483, 0x5cb0a9dcbd41fbd4, 0x76f988da831153b5,
        0x983e5152ee66dfab, 0xa831c66d2db43210, 0xb00327c898fb213f, 0xbf597fc7beef0ee4,
        0xc6e00bf33da88fc2, 0xd5a79147930aa725, 0x06ca6351e003826f, 0x142929670a0e6e70,
        0x27b70a8546d22ffc, 0x2e1b21385c26c926, 0x4d2c6dfc5ac42aed, 0x53380d139d95b3df,
        0x650a73548baf63de, 0x766a0abb3c77b2a8, 0x81c2c92e47edaee6, 0x92722c851482353b,
        0xa2bfe8a14cf10364, 0xa81a664bbc423001, 0xc24b8b70d0f89791, 0xc76c51a30654be30,
        0xd192e819d6ef5218, 0xd69906245565a910, 0xf40e35855771202a, 0x106aa07032bbd1b8,
        0x19a4c116b8d2d0c8, 0x1e376c085141ab53, 0x2748774cdf8eeb99, 0x34b0bcb5e19b48a8,
        0x391c0cb3c5c95a63, 0x4ed8aa4ae3418acb, 0x5b9cca4f7763e373, 0x682e6ff3d6b2b8a3,
        0x748f82ee5defb2fc, 0x78a5636f43172f60, 0x84c87814a1f0ab72, 0x8cc702081a6439ec,
        0x90befffa23631e28, 0xa4506cebde82bde9, 0xbef9a3f7b2c67915, 0xc67178f2e372532b,
        0xca273eceea26619c, 0xd186b8c721c0c207, 0xeada7dd6cde0eb1e, 0xf57d4f7fee6ed178,
        0x06f067aa72176fba, 0x0a637dc5a2c898a6, 0x113f9804bef90dae, 0x1b710b35131c471b,
        0x28db77f523047d84, 0x32caab7b40c72493, 0x3c9ebe0a15c9bebc, 0x431d67c49c100d4c,
        0x4cc5d4becb3e42b6, 0x597f299cfc657e2a, 0x5fcb6fab3ad6faec, 0x6c44198c4a475817
};


int next_block(FILE *f, union Block *M, enum Status *S, uint64_t *num_of_bits) {
    size_t num_of_bytes;

    if (*S == END) {
        return 0;
    } else if (*S == READ) {
        num_of_bytes = fread(M->bytes, 1, 128, f);
        *num_of_bits = *num_of_bits + (8 * num_of_bytes);
        if (num_of_bytes == 128) {
            return 1;
        } else if (num_of_bytes < 112) {
            M->bytes[num_of_bytes] = 0x80;
            for (num_of_bytes++; num_of_bytes < 112; num_of_bytes++) {
                M->bytes[num_of_bytes] = 0x00;
            }
            M->sixF[15] = (is_little_endian() ? bswap_64(*num_of_bits) : *num_of_bits);
            *S = END;
        } else {
            M->bytes[num_of_bytes] = 0x80;
            for (num_of_bytes++; num_of_bytes < 128; num_of_bytes++) {
                M->bytes[num_of_bytes] = 0x00;
            }
            *S = PAD;
        }
    } else if (*S == PAD) {
        for (num_of_bytes = 0; num_of_bytes < 112; num_of_bytes++) {
            M->bytes[num_of_bytes] = 0x00;
          }
        M->sixF[15] = (is_little_endian() ? bswap_64(*num_of_bits) : *num_of_bits);
        *S = END;
    }


    if (is_little_endian()) {
        for (int i = 0; i < 16; i++) {
            M->words[i] = bswap_64(M->words[i]);
        }
    }
    return 1;
}

int next_hash(union Block *M, WORD H[]) {
    WORD W[128];
    int t;
    WORD a, b, c, d, e, f, g, h, T1, T2;

    for (t = 0; t < 16; t++) W[t] = M->words[t];
    for (t = 16; t < 80; t++) W[t] = Sig1(W[t - 2]) + W[t - 7] + Sig0(W[t - 15]) + W[t - 16];

    a = H[0];
    b = H[1];
    c = H[2];
    d = H[3];
    e = H[4];
    f = H[5];
    g = H[6];
    h = H[7];

    for (t = 0; t < 80; t++) {
        T1 = h + SIG1(e) + CH(e, f, g) + K[t] + W[t];
        T2 = SIG0(a) + MAJ(a, b, c);
        h = g;
        g = f;
        f = e;
        e = d + T1;
        d = c;
        c = b;
        b = a;
        a = T1 + T2;
    }

    H[0] = a + H[0];
    H[1] = b + H[1];
    H[2] = c + H[2];
    H[3] = d + H[3];
    H[4] = e + H[4];
    H[5] = f + H[5];
    H[6] = g + H[6];
    H[7] = h + H[7];

    return 0;
}

int sha512(FILE *f, WORD H[]) {

    union Block M;


    uint64_t num_of_bits = 0;

    enum Status S = READ;


    while (next_block(f, &M, &S, &num_of_bits)) next_hash(&M, H);
    return 0;
}

int main(int argc, char *argv[]) {

    WORD H[] = {
            0x6a09e667f3bcc908,
            0xbb67ae8584caa73b,
            0x3c6ef372fe94f82b,
            0xa54ff53a5f1d36f1,
            0x510e527fade682d1,
            0x9b05688c2b3e6c1f,
            0x1f83d9abfb41bd6b,
            0x5be0cd19137e2179
    };

    FILE *f;

    const char *help = "\nType './sha512calculator --help' for more info. \n";
    if (argc == 2 && strcmp(argv[1], "--help") == 0) {
        printf("SHA-512 Calculator --help \n\nHash a file with the program by specifying a file. \n");
        printf("For example: './sha512calculator test_inputs/seasalt.txt'\n");
        printf("\nPlease make sure the file path and type is correct. \n");
        return 0;
    } 
    else if (argc > 2) {
        printf("Unreachable command. \n%s", help);
        return 1;
    } 
    else if (argc != 2) {
        printf("Expected filename in argument. \n%s", help);
        return 1;
    }
    if (!(f = fopen(argv[1], "r"))) {
        printf("Not able to read file %s \n%s", argv[1], help);
        return 1;
    }

    sha512(f, H);

    for (int i = 0; i < 8; i++)
        printf("%016" PF, H[i]);
    printf("  %s\n", argv[1]); 
    fclose(f);

    return 0;
}
