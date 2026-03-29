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
        bird->jtimer=30;
        bird->ysp=-11;
    }
    if (bird->ysp<10){
        bird->ysp+=0.4;
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

void update(const bool* keys, bool* playing, bool* playstart, Bird* bird){
    if (*playing){
        bird_jump(bird,keys);
        if (bird_collide(bird)){
            *playing = false;
            *playstart = false;
            bird_reset(bird);
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

void draw(SDL_Renderer* renderer, Bird* bird){
    SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255);
    SDL_RenderClear(renderer);

    bird_draw(bird, renderer);

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

        update(keys, playptr, playstartptr, birdptr);
        draw(renderer, birdptr);

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

    SDL_DestroyWindow(window);
    SDL_DestroyRenderer(renderer);
    TTF_DestroyRendererTextEngine(textEngine);
    
    SDL_Quit();
    return 0;
}