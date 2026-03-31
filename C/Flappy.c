#include<SDL3/SDL.h>
#include<SDL3/SDL_main.h>
#include<SDL3_ttf/SDL_ttf.h>
#include<stdlib.h>
#include<time.h>

struct timespec timespec_subtract(struct timespec* a, struct timespec* b){
    struct timespec result = {0};
    result.tv_sec = a->tv_sec-b->tv_sec;
    result.tv_nsec = a->tv_nsec-b->tv_nsec;
    if (result.tv_nsec<0){
        result.tv_sec--;
        result.tv_nsec+=1000000000;
    }
    return result;
}

typedef struct { int r, g, b; } Colour;

void renderCircle(SDL_Renderer* renderer, int x, int y, int rad, Colour colour){
    SDL_SetRenderDrawColor(renderer, colour.r, colour.g, colour.b, 255);
    for (int x2 = -rad; x2<=rad; x2++){
        for (int y2 = -rad; y2<=rad; y2++){
            if ((x2 * x2 + y2 * y2)<(rad*rad)){
                SDL_RenderPoint(renderer,x+x2,y+y2);
            }
        }
    }
}

void renderRect(SDL_Renderer* renderer, int x, int y, int w, int h, Colour colour){
    SDL_SetRenderDrawColor(renderer, colour.r, colour.g, colour.b, 255);
    SDL_FRect *rect = malloc(sizeof(SDL_FRect));
    *rect = (SDL_FRect) {x,y,w,h};
    SDL_RenderFillRect(renderer, rect);
    free(rect);
}

typedef struct {
    int num;
    int x;
    int y;
    int gap; //from centre y
    int width; //from centre x
} Pipe;

Pipe* newPipe(int num, int gap, int width){
    Pipe* pipe = malloc(sizeof(Pipe));
    *pipe = (Pipe) {num, 520*num, (rand()%(940-2*gap))+gap, gap, width};
    return pipe;
}

void drawPipe(Pipe* pipe, SDL_Renderer* renderer){
    renderRect(renderer, pipe->x-pipe->width, 0, 2*pipe->width, pipe->y-pipe->gap, (Colour) {0,255,0});
    renderRect(renderer, pipe->x-pipe->width, pipe->y+pipe->gap, 2*pipe->width, 940-(pipe->y+pipe->gap), (Colour) {0,255,0});
}

typedef struct {
    Pipe* pipe1;
    Pipe* pipe2;
} Pipes;

Pipes* newPipes(int gap, int width){
    Pipes* pipes = malloc(sizeof(Pipes));
    *pipes = (Pipes) {newPipe(1,gap,width),newPipe(2,gap,width)};
    return pipes;
}

void drawPipes(Pipes* pipes, SDL_Renderer* renderer){
    drawPipe(pipes->pipe1, renderer);
    drawPipe(pipes->pipe2, renderer);
}

void resetPipes(Pipes* pipes){
    pipes->pipe1->x=pipes->pipe1->num*520;
    pipes->pipe2->x=pipes->pipe2->num*520;
}

void DestroyPipes(Pipes* pipes){
    free(pipes->pipe1);
    free(pipes->pipe2);
    free(pipes);
}

typedef struct {
    int x;
    float y;
    int rad;
    float ysp;
    int jtimer;
} Bird;

Bird* newBird(){
    Bird* b = malloc(sizeof(Bird));
    if (b) {
        b->x=200;
        b->y=400;
        b->rad=25;
        b->ysp=0;
        b->jtimer=0;
    }
    return b;
}

void bird_jump(Bird* bird, const bool* keys){
    if (bird->jtimer>0){
        bird->jtimer--;
    } else if (keys[SDL_SCANCODE_W] || keys[SDL_SCANCODE_SPACE]){
        bird->jtimer=25;
        bird->ysp=-11;
    }
    if (bird->ysp<10){
        bird->ysp+=0.5;
    }
    bird->y += bird->ysp;
}

bool bird_collide(Bird* bird){
    if (bird->y-bird->rad<0 || bird->y+bird->rad>940){
        return true;
    }
    return false;
}

void bird_reset(Bird* bird){
    bird->ysp=0;
    bird->y=400;
    bird->jtimer=0;
}

void bird_draw(Bird* bird, SDL_Renderer* renderer){
    renderCircle(renderer, bird->x, bird->y, bird->rad, (Colour){255,255,0});
}

void update(const bool* keys, bool* playing, bool* playstart, Bird* bird, Pipes* pipes){
    if (*playing){
        bird_jump(bird,keys);
        if (bird_collide(bird)){
            *playing = false;
            *playstart = false;
            bird_reset(bird);
            resetPipes(pipes);
        }
    } else {
        if (keys[SDL_SCANCODE_W] || keys[SDL_SCANCODE_SPACE]){
            if (*playstart){
                *playing = true;
            }
        } else {
            *playstart = true;
        }
    }
}

void draw(SDL_Renderer* renderer, Bird* bird, Pipes* pipes){
    SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255);
    SDL_RenderClear(renderer);

    bird_draw(bird, renderer);
    drawPipes(pipes, renderer);

    SDL_RenderPresent(renderer);
}

int main(int argc, char* args[]){
    SDL_Window* window;                    // Declare a window pointer
    SDL_Renderer* renderer;
    TTF_TextEngine* textEngine;
    TTF_Init();
    bool done = false;
    SDL_Init(SDL_INIT_VIDEO);              // Initialize SDL3
    const bool *keys = SDL_GetKeyboardState(NULL);
    srand(time(NULL));

    //framerate stuff
    struct timespec inter = { .tv_sec = 0, .tv_nsec = 16666667};
    struct timespec start;
    struct timespec end;
    struct timespec span;
    struct timespec wait;
    //probably didn't have to have this many seperate variables

    bool* playptr = malloc(sizeof(bool));
    *playptr = false;
    bool* playstartptr = malloc(sizeof(bool));
    *playptr = false;
    Bird* birdptr = newBird();
    Pipes* pipesptr = newPipes(110,50);

    window = SDL_CreateWindow(
        "Flappy Bird",                  // title
        640,                               // width
        940,                               // height
        SDL_WINDOW_OPENGL                  // flags
    );

    renderer = SDL_CreateRenderer(window,NULL);
    textEngine = TTF_CreateRendererTextEngine(renderer);

    if (window == NULL) {
        SDL_LogError(SDL_LOG_CATEGORY_ERROR, "Could not create window: %s\n", SDL_GetError());
        return 1;
    }

    while (!done) {
        timespec_get(&start,TIME_UTC);

        SDL_Event event;
        while (SDL_PollEvent(&event)) {
            if (event.type == SDL_EVENT_QUIT) {
                done = true;
            }
        }
        if (keys[SDL_SCANCODE_ESCAPE]){
            done = true;
        }

        update(keys, playptr, playstartptr, birdptr, pipesptr);
        draw(renderer, birdptr, pipesptr);

        SDL_UpdateWindowSurface(window);

        timespec_get(&end,TIME_UTC);

        span = timespec_subtract(&end,&start);
        //printf("%d\n",(int)span.tv_nsec);
        if (!(span.tv_sec>inter.tv_sec || span.tv_nsec>inter.tv_nsec)){
            wait = timespec_subtract(&inter,&span);
            printf("%d\n",(int)wait.tv_nsec);
            nanosleep(&wait,NULL);
        }
    }

    free(birdptr);
    free(playptr);
    free(playstartptr);
    DestroyPipes(pipesptr);

    SDL_DestroyWindow(window);
    SDL_DestroyRenderer(renderer);
    TTF_DestroyRendererTextEngine(textEngine);
    
    SDL_Quit();
    return 0;
}