# Parte II – Análise de complexidade

As duas versões da Parte I (recursiva e iterativa) usam o **mesmo particionamento de Lomuto** e geram **exatamente as mesmas chamadas de `particionar`** sobre os mesmos intervalos. A versão iterativa só troca a pilha de chamadas da JVM por uma pilha explícita (`ArrayDeque`). Por isso, o tempo das duas é igual e toda a análise abaixo vale para ambas.

---

## i. Procedimento de particionamento (Lomuto)

```java
int pivo = v[fim];                       // custo constante
int i = ini - 1;                         // custo constante
for (int j = ini; j <= fim - 1; j++) {   // executa n - 1 vezes
    if (v[j] <= pivo) {                  // 1 comparação por iteração
        i = i + 1;                       // constante
        troca(v, i, j);                  // constante (3 atribuições)
    }
}
troca(v, i + 1, fim);                    // custo constante
return i + 1;
```

Seja `n = fim - ini + 1` o tamanho do subarranjo.

- Fora do laço há um número fixo de operações: ler o pivô, inicializar `i`, a troca final e o `return`. Chamamos esse custo de `c₂`.
- O laço percorre `j` de `ini` até `fim - 1`, ou seja, **sempre `n - 1` iterações**, qualquer que seja o conteúdo do vetor.
- Cada iteração faz uma comparação e, no máximo, um incremento e uma troca. Todas essas operações têm custo constante, limitado por `c₁`.

Então:

$$T_{part}(n) \le c_1 (n-1) + c_2 = O(n)$$

Para o limite inferior, basta notar que a comparação `v[j] <= pivo` é executada **obrigatoriamente** em todas as `n - 1` iterações. Mesmo no melhor cenário (nenhuma troca dentro do laço):

$$T_{part}(n) \ge c_0 (n-1) = \Omega(n)$$

Como valem os dois limites, $T_{part}(n) = \Theta(n)$. Esse é o termo $cn$ que aparece nas recorrências a seguir. Em particular, o Lomuto faz **exatamente $n-1$ comparações** entre elementos.

## ii. QuickSort com pivô aleatório (caso médio)

### Recorrência

Com pivô aleatório, cada um dos $n$ elementos tem a mesma chance $1/n$ de ser o pivô. Se o pivô escolhido for o $(k+1)$-ésimo menor elemento, a partição gera um lado esquerdo com $k$ elementos e um lado direito com $n-1-k$, para $k = 0, 1, \dots, n-1$. Fazendo a média sobre todas as possibilidades:

$$T(n) = cn + \frac{1}{n}\sum_{k=0}^{n-1} \big[T(k) + T(n-1-k)\big] = cn + \frac{1}{n}\sum_{k=0}^{n-1} T(k) + \frac{1}{n}\sum_{k=0}^{n-1} T(n-1-k)$$

### Por que os dois somatórios viram um só

Escrevendo os termos de cada somatório:

- primeiro: $T(0) + T(1) + \dots + T(n-1)$
- segundo: $T(n-1) + T(n-2) + \dots + T(0)$

São **os mesmos termos em ordem invertida**. Quando $k$ vai de $0$ a $n-1$, $n-1-k$ percorre os mesmos valores de trás para frente. Como a ordem das parcelas não altera a soma, os dois somatórios são iguais:

$$T(n) = cn + \frac{2}{n}\sum_{k=0}^{n-1} T(k)$$

Intuitivamente, cada tamanho de subproblema $k$ aparece duas vezes: uma quando sobra à esquerda do pivô e outra quando sobra à direita.

### Manipulação algébrica que elimina o somatório

1. Multiplicamos tudo por $n$ para tirar a fração:
$$nT(n) = cn^2 + 2\sum_{k=0}^{n-1} T(k) \quad (A)$$

2. Escrevemos a mesma equação para $n-1$:
$$(n-1)T(n-1) = c(n-1)^2 + 2\sum_{k=0}^{n-2} T(k) \quad (B)$$

3. Subtraímos (B) de (A). Os dois somatórios são iguais até $T(n-2)$, então quase tudo se cancela e só sobra o termo $2T(n-1)$:
$$nT(n) - (n-1)T(n-1) = c\,(n^2 - (n-1)^2) + 2T(n-1) = c(2n-1) + 2T(n-1)$$

4. Isolamos $nT(n)$:
$$nT(n) = (n+1)\,T(n-1) + c(2n-1)$$

O somatório desapareceu e agora $T(n)$ depende só de $T(n-1)$.

### Telescopagem até T(1)

Usando $c(2n-1) \le 2cn$ e dividindo os dois lados por $n(n+1)$:

$$\frac{T(n)}{n+1} \le \frac{T(n-1)}{n} + \frac{2c}{n+1}$$

Chamando $S(n) = \dfrac{T(n)}{n+1}$, a desigualdade fica $S(n) \le S(n-1) + \dfrac{2c}{n+1}$. Aplicamos a mesma relação para $S(n-1)$, depois para $S(n-2)$, e assim por diante. Cada passo acrescenta um termo novo e reduz o índice em 1:

$$
\begin{aligned}
S(n) &\le S(n-1) + \frac{2c}{n+1} \\
     &\le S(n-2) + \frac{2c}{n} + \frac{2c}{n+1} \\
     &\le S(n-3) + \frac{2c}{n-1} + \frac{2c}{n} + \frac{2c}{n+1} \\
     &\;\;\vdots \\
     &\le S(1) + 2c\sum_{k=3}^{n+1}\frac{1}{k}
\end{aligned}
$$

O nome "telescopagem" vem disso: a expressão vai "encolhendo" até chegar ao caso base $S(1) = T(1)/2$, que é uma constante.

### Relação com a série harmônica

A soma $H_m = \sum_{k=1}^{m} \frac{1}{k}$ é a **série harmônica**, e vale $H_m = \ln m + \gamma + O(1/m) \approx \ln m$, com $\gamma \approx 0{,}577$. Logo:

$$\sum_{k=3}^{n+1}\frac{1}{k} = H_{n+1} - 1 - \tfrac{1}{2} \le \ln(n+1)$$

Voltando para $T(n) = (n+1)\,S(n)$:

$$T(n) \le (n+1)\left[\frac{T(1)}{2} + 2c\ln(n+1)\right] = O(n \log n)$$

O mesmo raciocínio com $c(2n-1) \ge cn$ dá $T(n) \ge (n+1)\,[\,S(1) + c(H_{n+1} - 1{,}5)\,] = \Omega(n\log n)$. Portanto:

$$\boxed{T(n) = \Theta(n \log n)}$$

Contando só comparações (termo $n-1$ em vez de $cn$), a solução exata é $C(n) = 2(n+1)H_n - 4n \approx 2n\ln n \approx 1{,}39\, n\log_2 n$. Ou seja, no caso médio o QuickSort faz só cerca de 39% mais comparações que a partição perfeita.

### Por que vale para qualquer entrada

Sem aleatoriedade, a média acima seria tomada sobre as **entradas**, e uma entrada específica (por exemplo, um vetor já ordenado com pivô = último elemento) poderia ser sempre ruim. Com pivô aleatório, a aleatoriedade está **no algoritmo**. Para **qualquer vetor fixo**, cada elemento continua tendo probabilidade $1/n$ de ser o pivô, então a recorrência acima vale para todo vetor: ordenado, invertido ou embaralhado. O resultado $\Theta(n\log n)$ é um **tempo esperado** que vale para toda entrada, e nenhuma entrada específica força o pior caso.

### O pior caso ainda pode ocorrer?

**Sim, mas com probabilidade desprezível.** O pior caso, $\Theta(n^2)$, acontece se o sorteio pegar o menor ou o maior elemento em toda chamada. A chance de isso ocorrer em todos os níveis é

$$\prod_{k=2}^{n}\frac{2}{k} = \frac{2^{n-1}}{n!},$$

que já é menor que $10^{-10}$ para $n = 20$. Isso depende de sorte ruim no sorteio, não da entrada.

> **Cuidado com elementos repetidos:** a análise supõe elementos distintos. Com Lomuto, se **todos os elementos forem iguais**, `v[j] <= pivo` é sempre verdadeiro, e a partição sempre gera $n-1$ e $0$ elementos. Isso dá $\Theta(n^2)$ **mesmo com pivô aleatório**. O particionamento de Hoare ou o particionamento em 3 vias evitam esse problema.

---

## iii. QuickSort proporcional

Suponha que o pivô sempre divida o subarranjo em partes de tamanho $\alpha n$ e $(1-\alpha)n$, com $0 < \alpha \le 1/2$ constante:

$$T(n) = T(\alpha n) + T((1-\alpha)n) + cn$$

**Ideia geral:** em cada nível da árvore, os subproblemas juntos somam no máximo $n$ elementos, já que o pivô sai e nada é duplicado. Assim, cada nível custa **no máximo $cn$** (exatamente $cn$ enquanto nenhum ramo terminou). O ramo que sempre segue o lado $\alpha$ encolhe mais rápido e termina primeiro. O ramo que segue o lado $(1-\alpha)$ é o mais longo.

### α = 1/2 (partição 50/50)

```
Nível 0:                    n                              custo: cn
                    /                \
Nível 1:          n/2                n/2                   custo: cn/2 + cn/2 = cn
                /     \            /     \
Nível 2:      n/4     n/4        n/4     n/4               custo: 4 · cn/4 = cn
             /  \    /  \       /  \    /  \
Nível 3:   n/8  n/8 n/8 n/8   n/8  n/8 n/8  n/8            custo: 8 · cn/8 = cn
             ⋮                                              ⋮
Folhas:    1 1 1 1 1 1 1 1 ... 1                           (n folhas)
```

- **hmin = hmax**: o tamanho no nível $i$ é $n/2^i$, que chega a 1 quando $i = \log_2 n$. Todos os ramos têm a mesma altura: $h_{min} = h_{max} = \log_2 n$.
- **Limites:** todos os níveis custam exatamente $cn$, então $T(n) = cn\log_2 n + \Theta(n)$. O limite inferior e o superior coincidem: $cn\log_2 n \le T(n) \le cn(\log_2 n + 1)$.
- **Constante k:** $h_{max} = 1 \cdot \log_2 n \Rightarrow k = 1$.

### α = 1/4 (partição 75/25)

```
Nível 0:                                n                                      custo: cn
                         /                             \
Nível 1:              n/4                             3n/4                     custo: cn/4 + 3cn/4 = cn
                    /     \                         /       \
Nível 2:         n/16     3n/16                 3n/16       9n/16              custo: (1+3+3+9)cn/16 = cn
                 /  \      /   \                /    \       /    \
Nível 3:     n/64 3n/64 3n/64 9n/64        3n/64 9n/64  9n/64 27n/64           custo: (1+3+3+9+3+9+9+27)cn/64 = cn
                                                                       ⋮
  ramo mais curto (sempre 1/4) termina primeiro;  ramo mais longo (sempre 3/4) termina por último
```

- **hmin** (ramo que sempre pega $1/4$): o tamanho é $n/4^i = 1 \Rightarrow h_{min} = \log_4 n = \tfrac{1}{2}\log_2 n$.
- **hmax** (ramo que sempre pega $3/4$): o tamanho é $(3/4)^i n = 1 \Rightarrow h_{max} = \log_{4/3} n$.
- **Limite inferior:** os $h_{min}$ primeiros níveis estão completos e cada um custa exatamente $cn$, então
  $T(n) \ge cn\log_4 n = \Omega(n\log n)$.
- **Limite superior:** são no máximo $h_{max}+1$ níveis, e nenhum custa mais que $cn$, então
  $T(n) \le cn(\log_{4/3} n + 1) = O(n\log n)$.
- **Constante k:** $h_{max} = \log_{4/3} n = \dfrac{\log_2 n}{\log_2(4/3)} = \dfrac{\log_2 n}{0{,}415} \Rightarrow k \approx 2{,}41$.

Com uma partição 75/25, a árvore fica no máximo cerca de 2,4 vezes mais alta que a da partição perfeita.

### Resumo para todos os valores de α

Em geral, $h_{min} = \log_{1/\alpha} n$, $h_{max} = \log_{1/(1-\alpha)} n$ e $k = \dfrac{1}{\log_2\!\big(1/(1-\alpha)\big)}$.

| α | Partição | $h_{min}$ | $h_{max}$ | $k$ | Limite inferior | Limite superior |
|---|---|---|---|---|---|---|
| 1/2 | 50/50 | $\log_2 n$ | $\log_2 n$ | 1,000 | $cn\log_2 n$ | $cn(\log_2 n+1)$ |
| 1/3 | 67/33 | $\log_3 n \approx 0{,}63\log_2 n$ | $\log_{3/2} n$ | 1,710 | $cn\log_3 n$ | $cn(\log_{3/2} n+1)$ |
| 1/4 | 75/25 | $\log_4 n = 0{,}5\log_2 n$ | $\log_{4/3} n$ | 2,409 | $cn\log_4 n$ | $cn(\log_{4/3} n+1)$ |
| 1/5 | 80/20 | $\log_5 n \approx 0{,}43\log_2 n$ | $\log_{5/4} n$ | 3,106 | $cn\log_5 n$ | $cn(\log_{5/4} n+1)$ |

### Conclusão para qualquer α constante

Para **qualquer** $\alpha$ constante com $0 < \alpha \le 1/2$, os limites inferior e superior são $cn$ vezes um logaritmo de $n$. Os dois diferem só pela base do logaritmo, e trocar de base é multiplicar por uma constante. Logo:

$$\boxed{T(n) = \Theta(n\log n) \text{ para todo } \alpha \text{ constante}}$$

O valor de $\alpha$ só altera a **constante** $k$. Quanto mais desbalanceada a partição, maior o $k$. Mesmo uma divisão 99/1 continua $\Theta(n\log n)$, com $k \approx 69$.

### E se a partição não for proporcional?

Se a partição gera sempre **0 e n − 1** elementos (o que acontece, por exemplo, com pivô = último elemento num vetor já ordenado), a recorrência vira:

$$T(n) = T(n-1) + T(0) + cn$$

A árvore degenera numa "lista": cada nível tira só **um** elemento (o pivô), então a altura é $n - 1$ em vez de logarítmica. Somando os custos dos níveis:

$$T(n) = cn + c(n-1) + c(n-2) + \dots + c \cdot 1 = c\,\frac{n(n+1)}{2} = \Theta(n^2)$$

A diferença essencial é esta: na partição proporcional, o tamanho do subproblema **cai por um fator constante** a cada nível ($n \to (1-\alpha)n$), o que gera altura logarítmica. Na partição 0 / n−1, ele **cai por uma quantidade constante** ($n \to n-1$), o que gera altura linear. O mesmo $\Theta(n^2)$ aparece se o lado menor tiver sempre um tamanho fixo (1, 2, 10, ... elementos), porque o que importa é a proporção, não o número absoluto.

---

## Observação sobre espaço (versão recursiva × iterativa)

O tempo é o mesmo nas duas versões, mas o uso de memória extra merece um comentário:

- **Recursiva:** a profundidade da pilha de chamadas é a altura da árvore. Fica em $O(\log n)$ no caso médio ou proporcional e chega a $O(n)$ no pior caso.
- **Iterativa:** a pilha explícita guarda intervalos pendentes e tem o mesmo comportamento. Empilhando sempre o intervalo **maior** primeiro (para processar o menor antes), dá para garantir pilha $O(\log n)$ mesmo no pior caso.
